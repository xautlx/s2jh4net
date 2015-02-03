/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.security;

import java.io.Serializable;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.SourceUsernamePasswordToken.AuthSourceEnum;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;

/**
 * 存放在权限框架容器中的认证授权用户数据对象
 */
public class AuthUserDetails implements Serializable {

    private static final long serialVersionUID = 8346793124666695534L;

    @MetaData(value = "超级管理员角色")
    public final static String ROLE_SUPER_USER = "ROLE_SUPER_USER";

    @MetaData(value = "前端门户用户角色")
    public final static String ROLE_SITE_USER = "ROLE_SITE_USER";

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
    private AuthSourceEnum source = AuthSourceEnum.W;

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
        if (AuthSourceEnum.A.equals(source)) {
            return "/admin";
        } else if (AuthSourceEnum.M.equals(source)) {
            return "/m";
        } else {
            return "/w";
        }
    }

    @Override
    public String toString() {
        return "AuthUserDetails [authGuid=" + authGuid + ", authUid=" + authUid + ", authType=" + authType
                + ", nickName=" + nickName + ", source=" + source + "]";
    }
}
