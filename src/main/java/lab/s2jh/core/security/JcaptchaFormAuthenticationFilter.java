package lab.s2jh.core.security;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.security.SourceUsernamePasswordToken.AuthSourceEnum;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.IPAddrFetcher;
import lab.s2jh.core.web.captcha.ImageCaptchaServlet;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class JcaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

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

    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        SourceUsernamePasswordToken token = new SourceUsernamePasswordToken(username, password, rememberMe, host);
        String source = request.getParameter("source");
        if (StringUtils.isNotBlank(source)) {
            token.setSource(Enum.valueOf(AuthSourceEnum.class, source));
        }
        return token;
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
                    String captcha = request.getParameter(captchaParam);
                    if (!ImageCaptchaServlet.validateResponse((HttpServletRequest) request, captcha)) {
                        throw new CaptchaValidationException("验证码不正确");
                    }
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
            User authAccount = userService.findByAuthTypeAndAuthUid(User.AuthTypeEnum.SYS, sourceUsernamePasswordToken.getUsername());
            if (authAccount != null) {
                authAccount.setLogonTimes(authAccount.getLogonTimes() + 1);
                authAccount.setLastLogonFailureTime(new Date());
                authAccount.setLogonFailureTimes(authAccount.getLogonFailureTimes() + 1);
                userService.save(authAccount);

                //达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
                if (authAccount.getLogonFailureTimes() > LOGON_FAILURE_LIMIT) {
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

        //写入登入记录信息
        UserLogonLog userLogonLog = new UserLogonLog();
        userLogonLog.setLogonTime(new Date());
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

        //根据不同登录类型转向不同成功界面
        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();

        //判断密码是否已到期，如果是则转向密码修改界面
        Date credentialsExpireTime = authAccount.getCredentialsExpireTime();
        if (credentialsExpireTime != null && credentialsExpireTime.before(new Date())) {
            httpServletResponse.sendRedirect(authUserDetails.getUrlPrefixBySource() + "/profile/cpwd");
            return false;
        }

        return super.onLoginSuccess(token, subject, request, httpServletResponse);
    }

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

    public static class CaptchaValidationException extends AuthenticationException {

        private static final long serialVersionUID = -7285314964501172092L;

        public CaptchaValidationException(String message) {
            super(message);
        }
    }

}
