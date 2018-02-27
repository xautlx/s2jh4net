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

import com.entdiy.auth.service.UserService;
import lombok.Setter;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AppLogoutFilter extends LogoutFilter {

    @Setter
    private UserService userService;

    @Override
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) {
        //TODO 基于accessToken找到登录记录相关处理
    }
}
