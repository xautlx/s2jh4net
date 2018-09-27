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
package com.entdiy.core.security;

import com.entdiy.core.cons.GlobalConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 审计操作需要提取的登录用户信息接口定义
 */
public interface AuthUserDetails {
    /**
     * 用户输入的登录账号
     *
     * @return
     */
    String getUsername();

    /**
     * 考虑到登录账号可能存在被修改的，为了数据排查的准确性
     * 额外把用户的唯一标识信息记录下来，以便准确的进行数据关联排查
     *
     * @return
     */
    Long getAccountId();

    /**
     * 数据访问控制域
     *
     * @return
     */
    @JsonIgnore
    String getDataDomain();

    /**
     * 记录当前OAuth登录openid
     *
     * @return
     */
    String getOauthOpenId();

    /**
     * 记录当前OAuth登录类型
     *
     * @return
     */
    GlobalConstant.OauthTypeEnum getOauthType();
}
