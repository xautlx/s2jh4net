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
package com.entdiy.sys.web;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.sys.entity.AccountMessage;
import com.entdiy.sys.service.AccountMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/sys/account-message")
public class AccountMessageController extends BaseController<AccountMessage, Long> {

    @Autowired
    private AccountMessageService accountMessageService;

    @MenuData("配置管理:系统管理:消息管理")
    @RequiresPermissions("配置管理:系统管理:消息管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity AccountMessage entity, Model model) {
        return "admin/sys/accountMessage-index";
    }

    @RequiresPermissions("配置管理:系统管理:消息管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<AccountMessage> findByPage(@ModelPropertyFilter(AccountMessage.class) GroupPropertyFilter filter,
                                           @ModelPageableRequest Pageable pageable) {
        return accountMessageService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:系统管理:消息管理")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        return "admin/sys/accountMessage-inputBasic";
    }
}
