/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.security;

import com.entdiy.aud.entity.AccountLogonLog;
import com.entdiy.aud.service.AccountLogonLogService;
import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.IPAddrFetcher;
import com.entdiy.core.util.UidUtils;
import com.entdiy.core.web.captcha.CaptchaUtils;
import com.entdiy.core.web.captcha.CaptchaValidationException;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class JcaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JcaptchaFormAuthenticationFilter.class);

    public static final Integer LOGON_FAILURE_LIMIT = 2;

    /**
     * 达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
     */
    public static final String KEY_AUTH_CAPTCHA_REQUIRED = "captchaRequired";

    /**
     * 密码失效剩余天数，放入Request属性传递给客户端，根据需要引导用户设置新密码
     */
    public static final String KEY_CREDENTIALS_EXPIRE_IN_AYS = "credentialsExpireInDays";

    /**
     * 记录用户输入的用户名信息，用于登录界面回显
     */
    public static final String KEY_AUTH_USERNAME_VALUE = "lastUserName";

    /**
     * 默认验证码参数名称
     */
    public static final String DEFAULT_VALIDATE_CODE_PARAM = "captcha";

    /**
     * 验证码参数名称
     */
    @Setter
    private String captchaParam = DEFAULT_VALIDATE_CODE_PARAM;

    @Setter
    private String passwordRefreshUrl;

    @Setter
    private Account.AuthTypeEnum authType;

    @Setter
    private AccountService accountService;

    @Setter
    private AccountLogonLogService accountLogonLogService;

    @Override
    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        Account.AuthTypeEnum authType = Account.AuthTypeEnum.site;
        String authTypeParam = request.getParameter("authType");
        if (StringUtils.isNotBlank(authTypeParam)) {
            authType = Account.AuthTypeEnum.valueOf(authTypeParam);
        }
        return new AuthTypeUsernamePasswordToken(authType, username, password, rememberMe, host);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " + "Authentication url ["
                        + getLoginUrl() + "]");
            }

            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                //如果是APP接口访问或AJAX请求，直接返回401状态码替代302便于客户端处理
                if ("XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    //追加loginUrl到响应头，方便AJAX转向登录界面
                    String loginUrl = getLoginUrl();
                    if (loginUrl.startsWith("/")) {
                        loginUrl = httpServletRequest.getContextPath() + loginUrl;
                    }
                    httpServletResponse.addHeader(HttpHeaders.LOCATION, loginUrl);
                    //return false 不在继续调用过滤链，直接返回响应
                    return false;
                }
            }

            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordToken token = (UsernamePasswordToken) createToken(request, response);
        String username = getUsername(request);
        Account account = accountService.findByUsername(authType, username);
        try {
            if (account != null) {
                //失败LOGON_FAILURE_LIMIT次，强制要求验证码验证
                if (account.getLastFailureTimes() > LOGON_FAILURE_LIMIT) {
                    CaptchaUtils.assetValidateCaptchaCode(request, captchaParam);
                }

                if (Boolean.FALSE.equals(account.getAccountNonLocked())) {
                    throw new LockedAccountException("账号已锁定停用");
                }

                LocalDate accountExpireDate = account.getAccountExpireDate();
                if (accountExpireDate != null && accountExpireDate.isAfter(DateUtils.currentDateTime().toLocalDate())) {
                    throw new DisabledAccountException("账号已到期停用");
                }

                Subject subject = getSubject(request, response);
                subject.login(token);
                return onLoginSuccess(account, token, subject, request, response);
            } else {
                return onLoginFailure(account, token, new UnknownAccountException("登录账号或密码不正确"), request, response);
            }
        } catch (AuthenticationException e) {
            return onLoginFailure(account, token, e, request, response);
        }
    }

    /**
     * 重写父类方法，当登录失败次数大于allowLoginNum（允许登录次）时，将显示验证码
     */
    protected boolean onLoginFailure(Account account, AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        //写入登录账号名称用于回显
        request.setAttribute(KEY_AUTH_USERNAME_VALUE, username);

        if (e instanceof CaptchaValidationException) {
            request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
        } else if (e instanceof IncorrectCredentialsException) {
            //消息友好提示
            e = new IncorrectCredentialsException("登录账号或密码不正确");

            //失败记录
            if (account != null) {
                //最近连续失败次数累加，超过一定次数强制要求验证码
                account.setLastFailureTimes(account.getLastFailureTimes() + 1);
                //总计认证失败次数累加，用于异常账户判断
                account.setLogonFailureTimes(account.getLogonFailureTimes() + 1);
                //记录最后登录失败时间
                account.setLastLogonFailureTime(DateUtils.currentDateTime());
                account.setLogonFailureTimes(account.getLogonFailureTimes() + 1);
                accountService.save(account);


                //达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
                if (account.getLogonFailureTimes() > LOGON_FAILURE_LIMIT) {
                    request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
                }
            }
        }
        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * 重写父类方法，当登录成功后，重置失败标志
     */
    protected boolean onLoginSuccess(Account account, AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        LocalDateTime now = DateUtils.currentDateTime();
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();

        //密码失效剩余天数，放入Request属性传递给客户端，根据需要引导用户设置新密码
        if (account.getCredentialsExpireDate() != null) {
            Period period = Period.between(account.getCredentialsExpireDate(), now.toLocalDate());
            request.setAttribute(KEY_CREDENTIALS_EXPIRE_IN_AYS, period.getDays());
        }

        //连续失败次数清零
        account.setLastFailureTimes(0);
        account.setLastLogonSuccessTime(now);
        account.setLogonSuccessTimes(account.getLogonSuccessTimes() + 1);
        if (StringUtils.isBlank(account.getAccessToken())) {
            account.setAccessToken(UidUtils.buildUID());
        }
        accountService.save(account);

        //写入登入记录信息
        AccountLogonLog accountLogonLog = new AccountLogonLog();
        accountLogonLog.setAccount(account);
        accountLogonLog.setLogonTime(now);
        accountLogonLog.setLogonDate(now.toLocalDate());
        accountLogonLog.setLogonTimes(account.getLogonSuccessTimes());
        accountLogonLog.setRemoteAddr(httpServletRequest.getRemoteAddr());
        accountLogonLog.setRemoteHost(httpServletRequest.getRemoteHost());
        accountLogonLog.setRemotePort(httpServletRequest.getRemotePort());
        accountLogonLog.setLocalAddr(httpServletRequest.getLocalAddr());
        accountLogonLog.setLocalName(httpServletRequest.getLocalName());
        accountLogonLog.setLocalPort(httpServletRequest.getLocalPort());
        accountLogonLog.setServerIP(IPAddrFetcher.getGuessUniqueIP());
        accountLogonLog.setHttpSessionId(httpServletRequest.getSession().getId());
        accountLogonLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        accountLogonLog.setXforwardFor(IPAddrFetcher.getRemoteIpAddress(httpServletRequest));
        accountLogonLogService.save(accountLogonLog);

        //根据不同登录类型转向不同成功界面 TODO JSON接口和页面交互判断处理
        if (true) {
            return true;
        } else {
            //判断密码是否已到期，如果是则转向密码修改界面
            LocalDate getCredentialsExpireDate = account.getCredentialsExpireDate();
            if (getCredentialsExpireDate != null && getCredentialsExpireDate.isBefore(now.toLocalDate())) {
                httpServletResponse.sendRedirect(passwordRefreshUrl);
                return false;
            }
            return super.onLoginSuccess(token, subject, request, httpServletResponse);
        }
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        //写入认证异常对象用于错误显示
        request.setAttribute(getFailureKeyAttribute(), ae);
    }
}
