package com.entdiy.security;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.security.SourceUsernamePasswordToken.AuthSourceEnum;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class BearerTokenRealm extends AuthorizingRealm {

    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BearerAuthenticationToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        BearerAuthenticationToken bearerToken = (BearerAuthenticationToken) authcToken;

        String accessToken = bearerToken.getAccessToken();

        User authAccount = userService.findByAuthTypeAndAccessToken(bearerToken.getAuthType(), accessToken);
        if (authAccount == null) {
            throw new UnknownAccountException("登录信息不正确");
        }

        //构造权限框架认证用户信息对象
        AuthUserDetails authUserDetails = ShiroJdbcRealm.buildAuthUserDetails(authAccount);
        authUserDetails.setSource(AuthSourceEnum.APP);
        authUserDetails.setAccessToken(authAccount.getAccessToken());

        return new SimpleAuthenticationInfo(authUserDetails, accessToken, "Bearer Token Realm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(AuthUserDetails.ROLE_APP_USER);
        return info;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
