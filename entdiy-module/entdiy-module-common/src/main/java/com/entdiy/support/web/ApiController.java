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
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.security.admin.AdminFormAuthenticationFilter;
import com.entdiy.security.annotation.AuthAccount;
import com.entdiy.sys.service.AccountMessageService;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(GlobalConstant.API_MAPPING_PREFIX)

@JsonView(JsonViews.App.class)
public class ApiController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMessageService accountMessageService;

    @ApiOperation("验证API接口服务状态及获取基本信息")
    @GetMapping("/pub/ping")
    @ResponseBody
    public OperationResult<ModelMap> pubPing() {
        ModelMap data = new ModelMap();
        data.put("datetime", DateUtils.currentDateTime().format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER));
        return OperationResult.buildSuccessResult(data);
    }

    @ApiIgnore
    @GetMapping("/ping")
    @ResponseBody
    public OperationResult<ModelMap> ping() {
        ModelMap data = new ModelMap();
        data.put("datetime", DateUtils.currentDateTime().format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER));
        return OperationResult.buildSuccessResult(data);
    }

    @ApiIgnore
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SITE_USER)
    @GetMapping("/ping/customer")
    @ResponseBody
    public OperationResult<ModelMap> pingCustomer(@ApiIgnore @AuthAccount Account account) {
        ModelMap data = new ModelMap();
        data.put("authUid", account.getAuthUid());
        data.put("accessToken", account.getAccessToken());
        return OperationResult.buildSuccessResult(data);
    }

    @ApiIgnore
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @GetMapping("/ping/super")
    @ResponseBody
    public OperationResult<ModelMap> pingSuperUser(@ApiIgnore @AuthAccount Account account) {
        ModelMap data = new ModelMap();
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

    @ApiOperation("用户未读消息数目")
    @RequestMapping(value = "/account-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult accountMessageCount(@ApiIgnore @AuthAccount Account account) {
        return OperationResult.buildSuccessResult(accountMessageService.findCountToRead(account));
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult modifyPasswordSave(@AuthAccount Account account, @RequestParam("oldpasswd") String oldpasswd,
                                              @RequestParam("newpasswd") String newpasswd) {
        Validation.notDemoMode();
        String encodedPasswd = accountService.encodeUserPasswd(account, oldpasswd);
        if (!encodedPasswd.equals(account.getPassword())) {
            return OperationResult.buildFailureResult("原密码不正确,请重新输入");
        } else {
            //更新密码失效日期为6个月后
            account.setCredentialsExpireDate(DateUtils.currentDateTime().plusMonths(6).toLocalDate());
            accountService.save(account, newpasswd);
            return OperationResult.buildSuccessResult("密码修改成功");
        }
    }
}
