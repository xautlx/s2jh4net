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
import com.entdiy.auth.entity.OauthAccount;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.OauthAccountService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.annotation.AuthAccount;
import com.entdiy.support.service.MailService;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OauthAccountService oauthAccountService;

    @Autowired
    private MailService mailService;

    @RequiresUser
    @ModelAttribute
    public void prepareModel(@AuthAccount Account account, HttpServletRequest request, Model model) {
        User user = userService.findByAccount(account);
        model.addAttribute("user", user);
        model.addAttribute("account", user.getAccount());
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //出于安全考虑，只允许指定属性绑定更新
        binder.setAllowedFields("mobile", "email");
    }

    @MenuData("个人信息:个人配置")
    @RequiresUser
    @RequestMapping(value = "/admin/profile/edit", method = RequestMethod.GET)
    public String accountShow(@AuthAccount Account account, Model model) {
        model.addAttribute("validationRules", ServletUtils.buildValidateRules(Account.class));
        List<OauthAccount> oauthAccounts = oauthAccountService.findByAccountAndOauthTypeAndAuthType(
                account, GlobalConstant.OauthTypeEnum.WECHAT, Account.AuthTypeEnum.admin);
        model.addAttribute("oauthAccounts", oauthAccounts);
        return "admin/profile/profile-edit";
    }

    @RequiresUser
    @RequestMapping(value = "/admin/profile/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult accountSave(@ModelAttribute("account") Account account, Model model) {
        accountService.save(account);
        return OperationResult.buildSuccessResult("个人配置信息修改成功");
    }

    @RequestMapping(value = "/admin/profile/password", method = RequestMethod.GET)
    public String modifyPasswordShow(Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/profile/password-edit";
    }

    @MetaData("密码过期强制重置-显示")
    @RequestMapping(value = "/admin/profile/credentials-expire", method = RequestMethod.GET)
    public String profileCredentialsExpireShow() {
        return "admin/profile/credentials-expire";
    }
}
