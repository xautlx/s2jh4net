package lab.s2jh.core.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lab.s2jh.module.auth.entity.User.AuthTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class BearerTokenAuthenticatingFilter extends AuthenticatingFilter {

    public static final String KEY_ACCESS_TOKEN = "ACCESS-TOKEN";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = httpServletRequest.getHeader(KEY_ACCESS_TOKEN);
        String accessType = httpServletRequest.getHeader("ACCESS-TYPE");
        if (StringUtils.isBlank(accessType)) {
            return new BearerAuthenticationToken(AuthTypeEnum.SYS, accessToken);
        } else {
            return new BearerAuthenticationToken(Enum.valueOf(AuthTypeEnum.class, accessType), accessToken);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = httpServletRequest.getHeader(KEY_ACCESS_TOKEN);
        if (StringUtils.isNotBlank(accessToken)) {
            return executeLogin(request, response);
        }
        return true;
    }

    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        //写入认证异常对象用于错误显示
        request.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, ae);
    }
}
