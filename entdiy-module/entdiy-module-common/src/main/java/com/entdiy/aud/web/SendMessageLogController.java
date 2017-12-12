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
package com.entdiy.aud.web;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.aud.entity.SendMessageLog;
import com.entdiy.aud.entity.SendMessageLog.SendMessageTypeEnum;
import com.entdiy.aud.service.SendMessageLogService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.view.OperationResult;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@MetaData("发送消息记录管理")
@Controller
@RequestMapping(value = "/admin/aud/send-message-log")
public class SendMessageLogController extends BaseController<SendMessageLog, Long> {

    @Autowired
    private SendMessageLogService sendMessageLogService;

    @Override
    protected BaseService<SendMessageLog, Long> getEntityService() {
        return sendMessageLogService;
    }

    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }

    @MenuData("配置管理:系统记录:发送消息记录")
    @RequiresPermissions("配置管理:系统记录:发送消息记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("messageTypeMap", EnumUtils.getEnumDataMap(SendMessageTypeEnum.class));
        return "admin/aud/sendMessageLog-index";
    }

    @RequiresPermissions("配置管理:系统记录:发送消息记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<SendMessageLog> findByPage(HttpServletRequest request) {
        return super.findByPage(SendMessageLog.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/aud/sendMessageLog-inputTabs";
    }

    //@RequiresPermissions("TODO {PATH}:SendMessageLog")
    @RequiresPermissions("配置管理:系统记录:发送消息记录")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        model.addAttribute("messageTypeMap", EnumUtils.getEnumDataMap(SendMessageTypeEnum.class));
        return "admin/aud/sendMessageLog-inputBasic";
    }

    @Override
    @RequiresPermissions("配置管理:系统记录:发送消息记录")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") SendMessageLog entity) {
        return super.editSave(entity);
    }

    @Override
    @RequiresPermissions("配置管理:系统记录:发送消息记录")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids);
    }

}