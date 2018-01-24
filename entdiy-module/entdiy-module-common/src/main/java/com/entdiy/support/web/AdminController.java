/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.context.SpringPropertiesHolder;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.support.service.MailService;
import com.entdiy.sys.service.MenuService;
import com.entdiy.sys.vo.NavMenuVO;
import com.google.common.collect.Lists;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MailService mailService;

    @RequestMapping(value = {"/admin", "/admin/"}, method = RequestMethod.GET)
    public String adminIndex(HttpServletRequest request, Model model) {
        model.addAttribute("baiduMapAppkey", SpringPropertiesHolder.getProperty("baidu.map.appkey"));
        return "admin/index";
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public String login() {
        return "admin/pub/login";
    }

    /**
     * 表单的/login POST请求首先会被Shiro拦截处理，在认证失败之后才会触发调用此方法
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public String loginFailure(HttpServletRequest request, Model model) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae != null) {
            model.addAttribute("error", ae.getMessage());
            return "admin/pub/login";
        } else {
            return "redirect:/admin";
        }
    }

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        model.addAttribute("devMode", AppContextHolder.isDevMode());
        return "admin/dashboard";
    }

    /**
     * 计算显示用户登录菜单数据
     */
    @RequestMapping(value = "/admin/menus", method = RequestMethod.GET)
    @ResponseBody
    public List<NavMenuVO> navMenu() {
        User user = userService.findCurrentAuthUser();
        //如果未登录则直接返回空
        if (user == null) {
            return Lists.newArrayList();
        }
        return menuService.processUserMenu(user);
    }

    /**
     * 管理端忘记密码界面显示
     * 对应的POST处理直接调用账户通用处理逻辑
     *
     * @param request
     * @param model
     * @return
     * @see SupportAccountController#forgetPasswordSave(HttpServletRequest, String, String)
     */
    @RequestMapping(value = "/admin/pub/password/forget", method = RequestMethod.GET)
    public String forgetPasswordShow(HttpServletRequest request, Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/pub/password-forget";
    }

    /**
     * 管理端重置密码界面显示
     * 对应的POST处理直接调用账户通用处理逻辑
     *
     * @param request
     * @param model
     * @return
     * @see SupportAccountController#resetPasswordSave(HttpServletRequest, HttpServletResponse, String, String, String, String, String)
     */
    @RequestMapping(value = "/admin/pub/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(HttpServletRequest request, Model model) {
        return "admin/pub/password-reset";
    }
}
