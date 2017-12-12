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
package com.entdiy.sys.web;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.ConfigProperty;
import com.entdiy.sys.service.ConfigPropertyService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@MetaData("参数管理")
@Controller
@RequestMapping(value = "/admin/sys/config-property")
public class ConfigPropertyController extends BaseController<ConfigProperty, Long> {

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    protected BaseService<ConfigProperty, Long> getEntityService() {
        return configPropertyService;
    }

    @MenuData("配置管理:系统管理:参数配置")
    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/sys/configProperty-index";
    }

    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<ConfigProperty> findByPage(HttpServletRequest request) {
        return super.findByPage(ConfigProperty.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/sys/configProperty-inputTabs";
    }

    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow() {
        return "admin/sys/configProperty-inputBasic";
    }

    @MetaData("参数保存")
    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") ConfigProperty entity, HttpServletRequest request) {
        return super.editSave(entity);
    }

    @RequestMapping(value = "/html-preview", method = RequestMethod.POST)
    public String htmlPreview() {
        return "admin/sys/configProperty-htmlPreview";
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }
}
