/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.security;

import com.entdiy.aud.entity.UserLogonLog;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.UserExt;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.IPAddrFetcher;
import com.entdiy.core.util.UidUtils;
import com.entdiy.core.web.captcha.CaptchaUtils;
import com.entdiy.core.web.captcha.CaptchaValidationException;
import com.entdiy.security.SourceUsernamePasswordToken.AuthSourceEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JcaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JcaptchaFormAuthenticationFilter.class);

    public static final Integer LOGON_FAILURE_LIMIT = 2;

    /**
     * 达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
     */
    public static final String KEY_AUTH_CAPTCHA_REQUIRED = "auth_captcha_required";

    /**
     * 记录用户输入的用户名信息，用于登录界面回显
     */
    public static final String KEY_AUTH_USERNAME_VALUE = "auth_username_value";

    /**
     * 默认验证码参数名称
     */
    public static final String DEFAULT_VALIDATE_CODE_PARAM = "captcha";

    //验证码参数名称
    private String captchaParam = DEFAULT_VALIDATE_CODE_PARAM;

    private UserService userService;

    /**
     * 是否强制转向指定successUrl，忽略登录之前自动保存的URL
     */
    private boolean forceSuccessUrl = false;

    private boolean isMobileAppAccess(ServletRequest request) {
        //获取设备ID标识
        String uuid = request.getParameter("uuid");
        return StringUtils.isNotBlank(uuid);
    }

    @Override
    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        SourceUsernamePasswordToken token = new SourceUsernamePasswordToken(username, password, rememberMe, host);
        String source = request.getParameter("source");
        //获取设备ID标识
        String uuid = request.getParameter("uuid");
        token.setUuid(uuid);
        if (StringUtils.isNotBlank(source)) {
            token.setSource(Enum.valueOf(AuthSourceEnum.class, source));
        } else {
            if (isMobileAppAccess(request)) {
                token.setSource(AuthSourceEnum.APP);
            }
        }
        return token;
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
                if (isMobileAppAccess(request) || "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {
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
        SourceUsernamePasswordToken token = (SourceUsernamePasswordToken) createToken(request, response);
        try {
            String username = getUsername(request);
            //写入登录账号名称用于回显
            request.setAttribute(KEY_AUTH_USERNAME_VALUE, username);

            User authAccount = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, username);
            if (authAccount != null) {

                //失败LOGON_FAILURE_LIMIT次，强制要求验证码验证
                if (authAccount.getLogonFailureTimes() > LOGON_FAILURE_LIMIT) {
                    CaptchaUtils.assetValidateCaptchaCode(request, captchaParam);
                }

                Subject subject = getSubject(request, response);
                subject.login(token);
                return onLoginSuccess(token, subject, request, response);
            } else {
                return onLoginFailure(token, new UnknownAccountException("登录账号或密码不正确"), request, response);
            }
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * 重写父类方法，当登录失败次数大于allowLoginNum（允许登录次）时，将显示验证码
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (e instanceof CaptchaValidationException) {
            request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
        } else if (e instanceof IncorrectCredentialsException) {
            //消息友好提示
            e = new IncorrectCredentialsException("登录账号或密码不正确");
            //失败记录
            SourceUsernamePasswordToken sourceUsernamePasswordToken = (SourceUsernamePasswordToken) token;
            User user = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, sourceUsernamePasswordToken.getUsername());
            if (user != null) {
                UserExt userExt = user.getUserExt();
                userExt.setLogonTimes(userExt.getLogonTimes() + 1);
                userExt.setLastLogonFailureTime(DateUtils.currentDate());
                userService.saveExt(userExt);

                user.setLogonFailureTimes(user.getLogonFailureTimes() + 1);
                userService.save(user);

                //达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
                if (user.getLogonFailureTimes() > LOGON_FAILURE_LIMIT) {
                    request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
                }
            }
        }
        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * 重写父类方法，当登录成功后，重置失败标志
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        SourceUsernamePasswordToken sourceUsernamePasswordToken = (SourceUsernamePasswordToken) token;
        User authAccount = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, sourceUsernamePasswordToken.getUsername());
        Date now = DateUtils.currentDate();

        //最近认证失败次数清零
        authAccount.setLogonFailureTimes(0);
        //更新Access Token，并设置半年后过期
        if (StringUtils.isBlank(authAccount.getAccessToken()) || authAccount.getAccessTokenExpireTime().before(now)) {
            authAccount.setAccessToken(UidUtils.buildUID());
            authAccount.setAccessTokenExpireTime(new DateTime(DateUtils.currentDate()).plusMonths(6).toDate());
        }
        userService.save(authAccount);

        //写入登入记录信息
        UserLogonLog userLogonLog = new UserLogonLog();
        userLogonLog.setLogonTime(DateUtils.currentDate());
        userLogonLog.setLogonYearMonthDay(DateUtils.formatDate(userLogonLog.getLogoutTime()));
        userLogonLog.setRemoteAddr(httpServletRequest.getRemoteAddr());
        userLogonLog.setRemoteHost(httpServletRequest.getRemoteHost());
        userLogonLog.setRemotePort(httpServletRequest.getRemotePort());
        userLogonLog.setLocalAddr(httpServletRequest.getLocalAddr());
        userLogonLog.setLocalName(httpServletRequest.getLocalName());
        userLogonLog.setLocalPort(httpServletRequest.getLocalPort());
        userLogonLog.setServerIP(IPAddrFetcher.getGuessUniqueIP());
        userLogonLog.setHttpSessionId(httpServletRequest.getSession().getId());
        userLogonLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        userLogonLog.setXforwardFor(IPAddrFetcher.getRemoteIpAddress(httpServletRequest));
        userLogonLog.setAuthType(authAccount.getAuthType());
        userLogonLog.setAuthUid(authAccount.getAuthUid());
        userLogonLog.setAuthGuid(authAccount.getAuthGuid());
        userService.userLogonLog(authAccount, userLogonLog);

        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        authUserDetails.setAccessToken(authAccount.getAccessToken());

        //根据不同登录类型转向不同成功界面
        if (isMobileAppAccess(request)) {
            return true;
        } else {
            //判断密码是否已到期，如果是则转向密码修改界面
            Date credentialsExpireTime = authAccount.getCredentialsExpireTime();
            if (credentialsExpireTime != null && credentialsExpireTime.before(DateUtils.currentDate())) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + authUserDetails.getUrlPrefixBySource()
                        + "/profile/credentials-expire");
                return false;
            }

            //如果是强制转向指定successUrl则清空SavedRequest
            if (forceSuccessUrl) {
                WebUtils.getAndClearSavedRequest(httpServletRequest);
            }

            return super.onLoginSuccess(token, subject, request, httpServletResponse);
        }
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        //写入认证异常对象用于错误显示
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setForceSuccessUrl(boolean forceSuccessUrl) {
        this.forceSuccessUrl = forceSuccessUrl;
    }

}
