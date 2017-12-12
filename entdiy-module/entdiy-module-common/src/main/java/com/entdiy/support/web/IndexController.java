/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.support.web;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.filter.WebAppContextInitFilter;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.AuthContextHolder;
import com.entdiy.security.AuthUserDetails;
import com.entdiy.security.JcaptchaFormAuthenticationFilter;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.sys.service.UserProfileDataService;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private UserProfileDataService userProfileDataService;

    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model) {
        RequestUtil.appendGlobalProperties(request, model);
    }

    @RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
    public String unauthorizedUrl(HttpServletRequest request, Model model) {
        return "error/403";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = {"/admin", "/admin/"}, method = RequestMethod.GET)
    public String adminIndex(HttpServletRequest request, Model model) {
        model.addAttribute("baiduMapAppkey", dynamicConfigService.getString("baidu_map_appkey"));
        User user = AuthContextHolder.findAuthUser();
        model.addAttribute("layoutAttributes", userProfileDataService.findMapDataByUser(user));
        model.addAttribute("readFileUrlPrefix", ServletUtils.getReadFileUrlPrefix());
        model.addAttribute("globalProperties", dynamicConfigService.getAllPrperties());
        return "admin/index";
    }

    @RequestMapping(value = "/{source}/login", method = RequestMethod.GET)
    public String adminLogin(Model model, @PathVariable("source") String source) {
        //自助注册管理账号功能开关
        model.addAttribute("mgmtSignupEnabled", !dynamicConfigService.getBoolean(GlobalConstant.cfg_mgmt_signup_disabled, true));
        return source + "/login";
    }

    /**
     * <h3>APP接口: 登录。</h3>
     * <p>
     * <p>
     * 业务输入参数列表：
     * <ul>
     * <li><b>username</b> 账号</li>
     * <li><b>password</b> 密码</li>
     * <li><b>uuid</b> 设备或应用唯一标识</li>
     * </ul>
     * </p>
     * <p>
     * <p>
     * 业务输出参数列表：
     * <ul>
     * <li><b>token</b> 本次登录的随机令牌Token，目前设定半年有效期。APP取到此token值后存储在本应用持久化，在后续访问或下次重开应用时把此token以HTTP Header形式附在Request信息中：ACCESS-TOKEN={token}</li>
     * </ul>
     * </p>
     *
     * @return {@link OperationResult} 通用标准结构
     */
    @RequestMapping(value = "/app/login", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult appLogin(HttpServletRequest request, Model model) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae == null) {
            Map<String, Object> datas = Maps.newHashMap();
            datas.put("token", AuthContextHolder.getAuthUserDetails().getAccessToken());
            return OperationResult.buildSuccessResult(datas);
        } else {
            OperationResult result = OperationResult.buildFailureResult(ae.getMessage());
            Boolean captchaRequired = (Boolean) request.getAttribute(JcaptchaFormAuthenticationFilter.KEY_AUTH_CAPTCHA_REQUIRED);
            Map<String, Object> datas = Maps.newHashMap();
            datas.put("captchaRequired", captchaRequired);
            datas.put("captchaImageUrl", WebAppContextInitFilter.getInitedWebContextFullUrl() + "/pub/jcaptcha.servlet");
            result.setData(datas);
            return result;
        }
    }

    /**
     * PC站点方式登录失败，转向登录界面。表单的/login POST请求首先会被Shiro拦截处理，在认证失败之后才会触发调用此方法
     *
     * @param source  登录来源,  @see SourceUsernamePasswordToken.AuthSourceEnum
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{source}/login", method = RequestMethod.POST)
    public String loginFailure(@PathVariable("source") String source, HttpServletRequest request, Model model) {
        //自助注册管理账号功能开关
        model.addAttribute("mgmtSignupEnabled", !dynamicConfigService.getBoolean(GlobalConstant.cfg_mgmt_signup_disabled, true));

        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae != null) {
            model.addAttribute("error", ae.getMessage());
            return source + "/login";
        } else {
            return "redirect:/" + source;
        }
    }
}
