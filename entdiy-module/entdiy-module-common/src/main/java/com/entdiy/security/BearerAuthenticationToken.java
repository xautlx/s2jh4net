package com.entdiy.security;

import com.entdiy.auth.entity.User.AuthTypeEnum;

import org.apache.shiro.authc.AuthenticationToken;

public class BearerAuthenticationToken implements AuthenticationToken {

    private static final long serialVersionUID = 5369250527621928832L;

    private AuthTypeEnum authType;

    private String accessToken;

    public BearerAuthenticationToken(AuthTypeEnum authType, String accessToken) {
        this.authType = authType;
        this.accessToken = accessToken;
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AuthTypeEnum getAuthType() {
        return authType;
    }
}
