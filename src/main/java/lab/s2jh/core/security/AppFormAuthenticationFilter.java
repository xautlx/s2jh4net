package lab.s2jh.core.security;

import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.security.SourceUsernamePasswordToken.AuthSourceEnum;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.IPAddrFetcher;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class AppFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationFilter.class);

    private UserService userService;

    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        String host = getHost(request);
        SourceUsernamePasswordToken token = new SourceUsernamePasswordToken(username, password, false, host);
        token.setSource(AuthSourceEnum.P);
        token.setUuid(request.getParameter("uuid"));
        return token;
    }

    /**
     * 重写父类方法，当登录成功后，重置失败标志
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        SourceUsernamePasswordToken sourceUsernamePasswordToken = (SourceUsernamePasswordToken) token;
        User authAccount = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, sourceUsernamePasswordToken.getUsername());
        Date now = DateUtils.currentDate();

        //更新Access Token，并设置半年后过期
        if (StringUtils.isBlank(authAccount.getAccessToken()) || authAccount.getAccessTokenExpireTime().before(now)) {
            authAccount.setAccessToken(UUID.randomUUID().toString());
            authAccount.setAccessTokenExpireTime(new DateTime().plusMonths(6).toDate());
            userService.save(authAccount);
        }

        //写入登入记录信息
        UserLogonLog userLogonLog = new UserLogonLog();
        userLogonLog.setLogonTime(now);
        userLogonLog.setLogonYearMonthDay(DateUtils.formatDate(userLogonLog.getLogoutTime()));
        userLogonLog.setRemoteAddr(httpServletRequest.getRemoteAddr());
        userLogonLog.setRemoteHost(httpServletRequest.getRemoteHost());
        userLogonLog.setRemotePort(httpServletRequest.getRemotePort());
        userLogonLog.setLocalAddr(httpServletRequest.getLocalAddr());
        userLogonLog.setLocalName(httpServletRequest.getLocalName());
        userLogonLog.setLocalPort(httpServletRequest.getLocalPort());
        userLogonLog.setServerIP(IPAddrFetcher.getGuessUniqueIP());
        userLogonLog.setHttpSessionId(authAccount.getAccessToken());
        userLogonLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        userLogonLog.setXforwardFor(IPAddrFetcher.getRemoteIpAddress(httpServletRequest));
        userLogonLog.setAuthType(authAccount.getAuthType());
        userLogonLog.setAuthUid(authAccount.getAuthUid());
        userLogonLog.setAuthGuid(authAccount.getAuthGuid());
        userService.userLogonLog(authAccount, userLogonLog);

        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (e instanceof IncorrectCredentialsException) {
            //消息友好提示
            e = new IncorrectCredentialsException("登录账号或密码不正确");
            //失败记录
            SourceUsernamePasswordToken sourceUsernamePasswordToken = (SourceUsernamePasswordToken) token;
            User authAccount = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, sourceUsernamePasswordToken.getUsername());
            if (authAccount != null) {
                authAccount.setLogonTimes(authAccount.getLogonTimes() + 1);
                authAccount.setLastLogonFailureTime(DateUtils.currentDate());
                authAccount.setLogonFailureTimes(authAccount.getLogonFailureTimes() + 1);
                userService.save(authAccount);
            }
        }
        return super.onLoginFailure(token, e, request, response);
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

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }
    }

    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        //写入认证异常对象用于错误显示
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}