/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.security;

import com.entdiy.core.annotation.MetaData;

import org.apache.shiro.authc.UsernamePasswordToken;

public class SourceUsernamePasswordToken extends UsernamePasswordToken {

    public SourceUsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    private static final long serialVersionUID = 4494958942452530263L;

    @MetaData(value = "登录来源", comments = "标识是前端用户或后端管理等登录来源，可根据不同来源授予默认角色")
    private AuthSourceEnum source;

    @MetaData(value = "来源唯一标识", comments = "标识来源设备或应用等唯一标识")
    private String uuid;

    public enum AuthSourceEnum {

        @MetaData(value = "APP手机应用")
        APP,

        @MetaData(value = "HTML5 Mobile站点")
        H5,

        @MetaData(value = "WWW主站", comments = "source来源为空也表示此类型")
        DEFAULT,

        @MetaData(value = "Admin管理端")
        ADMIN
    }

    public AuthSourceEnum getSource() {
        return source;
    }

    public void setSource(AuthSourceEnum source) {
        this.source = source;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
