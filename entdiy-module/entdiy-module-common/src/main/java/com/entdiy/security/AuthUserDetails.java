/**
 * Copyright (c) 2012
 */
package com.entdiy.security;

import java.io.Serializable;

import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.security.SourceUsernamePasswordToken.AuthSourceEnum;

/**
 * 存放在权限框架容器中的认证授权用户数据对象
 */
public class AuthUserDetails implements Serializable {

    private static final long serialVersionUID = 8346793124666695534L;

    @MetaData(value = "超级管理员角色")
    public final static String ROLE_SUPER_USER = "ROLE_SUPER_USER";

    @MetaData(value = "前端门户用户角色")
    public final static String ROLE_SITE_USER = "ROLE_SITE_USER";

    @MetaData(value = "APP用户角色")
    public final static String ROLE_APP_USER = "ROLE_APP_USER";

    @MetaData(value = "后端管理用户角色")
    public final static String ROLE_MGMT_USER = "ROLE_MGMT_USER";

    @MetaData(value = "所有受控权限赋予此角色")
    public final static String ROLE_PROTECTED = "ROLE_PROTECTED";

    @MetaData(value = "账号全局唯一标识")
    private String authGuid;

    @MetaData(value = "账号类型所对应唯一标识")
    private String authUid;

    @MetaData(value = "账号类型")
    private AuthTypeEnum authType;

    @MetaData(value = "登录后友好显示昵称")
    private String nickName;

    @MetaData(value = "记录登录来源")
    private AuthSourceEnum source = AuthSourceEnum.DEFAULT;

    @MetaData(value = "访问TOKEN")
    private String accessToken;

    public String getAuthGuid() {
        return authGuid;
    }

    public void setAuthGuid(String authGuid) {
        this.authGuid = authGuid;
    }

    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public AuthTypeEnum getAuthType() {
        return authType;
    }

    public void setAuthType(AuthTypeEnum authType) {
        this.authType = authType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AuthSourceEnum getSource() {
        return source;
    }

    public void setSource(AuthSourceEnum source) {
        this.source = source;
    }

    public String getAuthDisplay() {
        return authType + ":" + authUid;
    }

    public String getUrlPrefixBySource() {
        if (AuthSourceEnum.ADMIN.equals(source)) {
            return "/admin";
        } else if (AuthSourceEnum.H5.equals(source)) {
            return "/m";
        } else {
            return "/w";
        }
    }

    @Override
    public String toString() {
        return "AuthUserDetails [authGuid=" + authGuid + ", authUid=" + authUid + ", authType=" + authType + ", nickName=" + nickName + ", source="
                + source + "]";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
