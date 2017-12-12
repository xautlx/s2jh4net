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
package com.entdiy.support.web;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.AuthContextHolder;
import com.entdiy.security.AuthUserDetails;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.support.service.MailService;
import com.entdiy.sys.entity.NotifyMessage.NotifyMessagePlatformEnum;
import com.entdiy.sys.entity.UserProfileData;
import com.entdiy.sys.service.NotifyMessageService;
import com.entdiy.sys.service.UserMessageService;
import com.entdiy.sys.service.UserProfileDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserProfileDataService userProfileDataService;

    @Autowired
    private DynamicConfigService dynamicConfigService;
    
    @ModelAttribute
    public void prepareModel(Model model) {
        User user = AuthContextHolder.findAuthUser();
        model.addAttribute("user", user);

        model.addAttribute("systemTitle",dynamicConfigService.getString("cfg_system_title"));
    }

    @MenuData("个人信息:个人配置")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/edit", method = RequestMethod.GET)
    public String profileLayoutShow(@ModelAttribute("user") User user, Model model) {
        user.addExtraAttributes(userProfileDataService.findMapDataByUser(user));
        return "admin/profile/profile-edit";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/layout", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult profileLayoutSave(@ModelAttribute("user") User user) {
        Map<String, Object> extraAttributes = user.getExtraAttributes();
        for (Map.Entry<String, Object> me : extraAttributes.entrySet()) {
            String code = me.getKey();
            UserProfileData entity = userProfileDataService.findByUserAndCode(user, code);
            if (entity == null) {
                entity = new UserProfileData();
                entity.setUser(user);
            }
            entity.setCode(code);
            entity.setValue(me.getValue().toString());
            userProfileDataService.save(entity);
        }
        return OperationResult.buildSuccessResult("界面布局配置参数保存成功");
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/password", method = RequestMethod.GET)
    public String modifyPasswordShow(Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/profile/password-edit";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/password", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult modifyPasswordSave(HttpServletRequest request, @RequestParam("oldpasswd") String oldpasswd,
            @RequestParam("newpasswd") String newpasswd) {
        User user = AuthContextHolder.findAuthUser();
        String encodedPasswd = userService.encodeUserPasswd(user, oldpasswd);
        if (!encodedPasswd.equals(user.getPassword())) {
            return OperationResult.buildFailureResult("原密码不正确,请重新输入");
        } else {
            //更新密码失效日期为6个月后
            user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
            userService.save(user, newpasswd);
            return OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码");
        }
    }

    @MetaData("密码过期强制重置-显示")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/credentials-expire", method = RequestMethod.GET)
    public String profileCredentialsExpireShow() {
        return "admin/profile/credentials-expire";
    }

    @MetaData("密码过期强制重置-更新")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/credentials-expire", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult profileCredentialsExpireSave(@RequestParam("newpasswd") String newpasswd) {
        User user = AuthContextHolder.findAuthUser();
        //更新密码失效日期为6个月后
        user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
        userService.save(user, newpasswd);
        return OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码").setRedirect("/admin");
    }

    @MetaData("用户未读公告数目")
    @RequestMapping(value = "/notify-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult notifyMessageCount(HttpServletRequest request,@ModelAttribute("user") User user) {
        String platform = request.getParameter("platform");
        if (StringUtils.isBlank(platform)) {
            platform = NotifyMessagePlatformEnum.web_admin.name();
        }

        return OperationResult.buildSuccessResult(notifyMessageService.findCountToRead(user, platform));
    }

    @MetaData("用户未读消息数目")
    @RequestMapping(value = "/user-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult userMessageCount(@ModelAttribute("user") User user) {
        if (user != null) {
            return OperationResult.buildSuccessResult(userMessageService.findCountToRead(user));
        } else {
            return OperationResult.buildSuccessResult(0);
        }
    }
}
