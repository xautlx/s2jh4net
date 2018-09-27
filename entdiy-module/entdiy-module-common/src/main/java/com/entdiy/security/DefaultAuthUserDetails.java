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

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(JsonViews.App.class)
    private String username;

    @MetaData(value = "昵称")
    @JsonView(JsonViews.App.class)
    private String nickname;

    @MetaData(value = "账号全局唯一标识")
    @JsonView(JsonViews.App.class)
    private Long accountId;

    @MetaData(value = "数据访问域")
    @JsonView(JsonViews.Admin.class)
    private String dataDomain;

    @MetaData(value = "访问TOKEN")
    @JsonView(JsonViews.App.class)
    private String accessToken;

    @MetaData(value = "Oauth认证类型")
    @JsonView(JsonViews.Admin.class)
    private GlobalConstant.OauthTypeEnum oauthType;

    @MetaData(value = "Oauth认证标识")
    @JsonView(JsonViews.Admin.class)
    private String oauthOpenId;

    @Override
    public String toString() {
        return username + "[" + accountId + "]";
    }
}
