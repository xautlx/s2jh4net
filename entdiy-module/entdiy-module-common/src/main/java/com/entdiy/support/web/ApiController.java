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
package com.entdiy.support.web;

import com.entdiy.auth.entity.Account;
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.security.admin.AdminFormAuthenticationFilter;
import com.entdiy.security.annotation.AuthAccount;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @ApiOperation("验证API接口服务状态及获取基本信息")
    @GetMapping("/ping")
    @ResponseBody
    public OperationResult ping() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("datetime", DateUtils.currentDateTime().format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER));
        return OperationResult.buildSuccessResult(data);
    }

    @ApiIgnore
    @RequiresUser
    @GetMapping("/ping/user")
    @ResponseBody
    public OperationResult pingUser(@AuthAccount Account account) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("authUid", account.getAuthUid());
        data.put("accessToken", account.getAccessToken());
        return OperationResult.buildSuccessResult(data);
    }

    @ApiIgnore
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @GetMapping("/ping/super")
    @ResponseBody
    public OperationResult pingSuperUser(@AuthAccount Account account) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("authUid", account.getAuthUid());
        data.put("accessToken", account.getAccessToken());
        return OperationResult.buildSuccessResult(data);
    }

    @ApiOperation("API访问登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", paramType = "query", required = true),
            @ApiImplicitParam(name = "authType", paramType = "query", required = false)
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult apiLogin(HttpServletRequest request) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae == null) {
            return OperationResult.buildSuccessResult(AuthContextHolder.getAuthUserDetails());
        } else {
            OperationResult result = OperationResult.buildFailureResult(ae.getMessage());
            Boolean captchaRequired = (Boolean) request.getAttribute(AdminFormAuthenticationFilter.KEY_AUTH_CAPTCHA_REQUIRED);
            Map<String, Object> datas = Maps.newHashMap();
            datas.put("captchaRequired", captchaRequired == null ? false : captchaRequired);
            datas.put("captchaImageUrl", request.getContextPath() + "/pub/jcaptcha.servlet");
            result.setData(datas);
            return result;
        }
    }

    @ApiOperation("API注销登录接口")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult apiLogout() {
        return OperationResult.buildSuccessResult();
    }
}
