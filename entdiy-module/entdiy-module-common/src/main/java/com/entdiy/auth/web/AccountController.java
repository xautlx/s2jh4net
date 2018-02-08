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
package com.entdiy.auth.web;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.util.JsonUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@MetaData("登录账户")
@Controller
@RequestMapping(value = "/admin/auth/account")
public class AccountController extends BaseController<Account, Long> {

    @Autowired
    private AccountService accountService;

    @MenuData("配置管理:权限管理:登录账户管理")
    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity Account entity, Model model) {
        model.addAttribute("authTypeJson", JsonUtils.writeValueAsString(EnumUtils.getEnumDataMap(Account.AuthTypeEnum.class)));
        return "admin/auth/account-index";
    }

    @RequiresPermissions(value = {"配置管理:权限管理:登录账户管理"}, logical = Logical.OR)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<Account> findByPage(@ModelPropertyFilter(Account.class) GroupPropertyFilter filter,
                                    @ModelPageableRequest Pageable pageable) {
        return accountService.findByPage(filter,pageable);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(@ModelEntity Account entity, HttpServletRequest request) {
        return "admin/auth/account-inputTabs";
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model, @ModelEntity Account entity) {
        return "admin/auth/account-inputBasic";
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity Account entity, Model model,
                                    @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        if (entity.isNew()) {
            Validation.isTrue(StringUtils.isNotBlank(rawPassword), "创建用户必须设置初始密码");
        }
        accountService.save(entity, rawPassword);
        return OperationResult.buildSuccessResult("数据保存处理完成", entity);
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/logon-data", method = RequestMethod.GET)
    public String editShow(@ModelEntity Account entity, Model model) {
        return "admin/auth/account-logonData";
    }
}