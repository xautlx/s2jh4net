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

import com.entdiy.auth.entity.SignupUser;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.auth.service.SignupUserService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.captcha.CaptchaUtils;
import com.entdiy.core.web.filter.WebAppContextInitFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.support.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/admin/pub")
public class AdminPubController {

    @Autowired
    private UserService userService;

    @Autowired
    private SignupUserService signupUserService;

    @Autowired
    private MailService mailService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @RequestMapping(value = "/password/forget", method = RequestMethod.GET)
    public String forgetPasswordShow(HttpServletRequest request, Model model) {
        RequestUtil.appendGlobalProperties(request, model);
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/pub/password-forget";
    }

    @RequestMapping(value = "/password/forget", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult forgetPasswordSave(HttpServletRequest request, @RequestParam("uid") String uid) {
        //二次校验验证码避免绕过表单校验的恶意请求
        CaptchaUtils.assetValidateCaptchaCode(request, "captcha");

        User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, uid);
        if (user == null) {
            user = userService.findByProperty("email", uid);
        }
        if (user == null) {
            return OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理");
        }
        String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            return OperationResult.buildFailureResult("当前账号未设定注册邮箱，请联系管理员先设置邮箱后再进行此操作");
        }

        userService.requestResetPassword(WebAppContextInitFilter.getInitedWebContextFullUrl(), user);
        return OperationResult.buildSuccessResult("找回密码请求处理成功。重置密码邮件已发送至：" + email);
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(HttpServletRequest request, Model model) {
        RequestUtil.appendGlobalProperties(request, model);
        return "admin/pub/password-reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult resetPasswordSave(HttpServletRequest request, HttpServletResponse response, @RequestParam("uid") String uid,
                                             @RequestParam("email") String email, @RequestParam("code") String code, @RequestParam("newpasswd") String newpasswd) throws IOException {
        User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, uid);
        if (user != null) {
            if (!email.equalsIgnoreCase(user.getEmail())) {
                return OperationResult.buildFailureResult("登录账号和邮箱地址不匹配，请检查或联系管理员");
            }
            if (code.equals(user.getUserExt().getRandomCode())) {
                //user.setRandomCode(null);
                //更新密码失效日期为6个月后
                user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
                userService.save(user, newpasswd);
                return OperationResult.buildSuccessResult("密码重置成功，您可以马上使用新设定密码登录系统啦").setRedirect("/admin/login");
            } else {
                return OperationResult.buildFailureResult("验证码不正确或已失效，请尝试重新找回密码操作");
            }
        }
        return OperationResult.buildFailureResult("操作失败");
    }

    @RequestMapping(value = "/admin/signup", method = RequestMethod.GET)
    public String signupShow(HttpServletRequest request, Model model) {
        RequestUtil.appendGlobalProperties(request, model);
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/pub/signup";
    }

    @RequestMapping(value = "/admin/signup", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult signupSave(HttpServletRequest request, @ModelAttribute("entity") SignupUser entity) {
        //二次校验验证码避免绕过表单校验的恶意请求
        CaptchaUtils.assetValidateCaptchaCode(request, "captcha");

        if (dynamicConfigService.getBoolean(GlobalConstant.cfg_mgmt_signup_disabled, false)) {
            return OperationResult.buildFailureResult("系统暂未开发账号注册功能，如有疑问请联系管理员");
        }
        signupUserService.signup(entity, request.getParameter("password"));
        return OperationResult.buildSuccessResult("注册成功。需要等待管理员审批通过后方可登录系统。");
    }

    /**
     * 验证手机唯一性
     * <p>
     * 业务输入参数列表：
     * <ul>
     * <li><b>mobile</b> 手机号</li>
     * </ul>
     * </p>
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/admin/signup/unique/mobile", method = RequestMethod.GET)
    @ResponseBody
    public boolean authMobileUnique(HttpServletRequest request) {
        String mobile = request.getParameter("mobile");
        if (!CollectionUtils.isEmpty(userService.findByMobile(mobile))) {
            return false;
        }
        if (!CollectionUtils.isEmpty(signupUserService.findByMobile(mobile))) {
            return false;
        }
        return true;
    }

    /**
     * 验证电子邮件唯一性
     * <p>
     * 业务输入参数列表：
     * <ul>
     * <li><b>email</b> 电子邮件</li>
     * </ul>
     * </p>
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/admin/signup/unique/email", method = RequestMethod.GET)
    @ResponseBody
    public boolean authEmailUnique(HttpServletRequest request) {
        String email = request.getParameter("email");
        if (!CollectionUtils.isEmpty(userService.findByEmail(email))) {
            return false;
        }
        if (!CollectionUtils.isEmpty(signupUserService.findByEmail(email))) {
            return false;
        }
        return true;
    }

    /**
     * 验证电子邮件唯一性
     * <p>
     * 业务输入参数列表：
     * <ul>
     * <li><b>email</b> 电子邮件</li>
     * </ul>
     * </p>
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/admin/signup/unique/uid", method = RequestMethod.GET)
    @ResponseBody
    public boolean authUidUnique(HttpServletRequest request) {
        String authUid = request.getParameter("authUid");
        if (userService.findByAuthUid(authUid) != null) {
            return false;
        }
        if (signupUserService.findByAuthUid(authUid) != null) {
            return false;
        }
        return true;
    }
}
