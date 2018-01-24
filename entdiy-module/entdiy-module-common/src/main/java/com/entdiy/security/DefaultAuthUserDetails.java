/**
 * Copyright (c) 2012
 */
package com.entdiy.security;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.security.AuthUserDetails;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 存放在权限框架容器中的认证授权用户数据对象
 */
@Getter
@Setter
public class DefaultAuthUserDetails implements Serializable, AuthUserDetails {

    private static final long serialVersionUID = 8346793124666695534L;

    @MetaData(value = "超级管理员角色")
    public final static String ROLE_SUPER_USER = "ROLE_SUPER_USER";

    @MetaData(value = "前端门户用户角色")
    public final static String ROLE_SITE_USER = "ROLE_SITE_USER";

    @MetaData(value = "后端管理用户角色")
    public final static String ROLE_MGMT_USER = "ROLE_MGMT_USER";

    @MetaData(value = "所有受控权限赋予此角色")
    public final static String ROLE_PROTECTED = "ROLE_PROTECTED";

    @MetaData(value = "登录标识")
    private String username;

    @MetaData(value = "账号全局唯一标识")
    private Long accountId;

    @MetaData(value = "数据访问域")
    private String dataDomain;

    @MetaData(value = "访问TOKEN")
    private String accessToken;

    @Override
    public String toString() {
        return username + "[" + accountId + "]";
    }
}
