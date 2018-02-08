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
package com.entdiy.aud.web;

import com.entdiy.aud.entity.LoggingEvent;
import com.entdiy.aud.entity.LoggingEvent.LoggingHandleStateEnum;
import com.entdiy.aud.service.LoggingEventService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.util.JsonUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.view.OperationResult;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@MetaData("日志事件管理")
@Controller
@RequestMapping(value = "/admin/aud/logging-event")
public class LoggingEventController extends BaseController<LoggingEvent, Long> {

    @Autowired
    private LoggingEventService loggingEventService;

    @MenuData("配置管理:系统记录:异常日志记录")
    @RequiresPermissions("配置管理:系统记录:异常日志记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity LoggingEvent entity, Model model) {
        model.addAttribute("stateJson", JsonUtils.writeValueAsString(EnumUtils.getEnumDataMap(LoggingHandleStateEnum.class)));
        return "admin/aud/loggingEvent-index";
    }

    @RequiresPermissions("配置管理:系统记录:异常日志记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<LoggingEvent> findByPage(@ModelPropertyFilter(LoggingEvent.class) GroupPropertyFilter filter,
                                         @ModelPageableRequest Pageable pageable) {
        return loggingEventService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:系统记录:异常日志记录")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        model.addAttribute("stateMap", EnumUtils.getEnumDataMap(LoggingHandleStateEnum.class));
        return "admin/aud/loggingEvent-inputBasic";
    }

    @RequiresPermissions("配置管理:系统记录:异常日志记录")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity LoggingEvent entity, Model model) {
        return super.editSave(entity);
    }
}