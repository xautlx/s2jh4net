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
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.NotifyMessage.NotifyMessagePlatformEnum;
import com.entdiy.sys.entity.NotifyMessageRead;
import com.entdiy.sys.service.DataDictService;
import com.entdiy.sys.service.NotifyMessageReadService;
import com.entdiy.sys.service.NotifyMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/sys/notify-message")
public class NotifyMessageController extends BaseController<NotifyMessage, Long> {

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private NotifyMessageReadService notifyMessageReadService;

    @Autowired
    private DataDictService dataDictService;

    @MenuData("配置管理:系统管理:公告管理")
    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity NotifyMessage entity, Model model) {
        return "admin/sys/notifyMessage-index";
    }

    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonPage<NotifyMessage> findByPage(@ModelPropertyFilter(NotifyMessage.class) GroupPropertyFilter filter,
                                              @ModelPageableRequest Pageable pageable) {
        return notifyMessageService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(@ModelEntity NotifyMessage entity, Model model) {
        model.addAttribute("platformMap", EnumUtils.getEnumDataMap(NotifyMessagePlatformEnum.class));
        model.addAttribute("messageTypeMap", dataDictService.findMapDataByRootPrimaryKey(GlobalConstant.DATADICT_MESSAGE_TYPE));
        return "admin/sys/notifyMessage-inputBasic";
    }

    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity NotifyMessage entity, Model model) {
        return super.editSave(notifyMessageService, entity);
    }

    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@ModelEntity NotifyMessage... entities) {
        return super.delete(notifyMessageService, entities, (entity) -> notifyMessageReadService.countByNotifyMessage(entity) > 0 ? "公告[" + entity.getTitle() + "]已经被阅读，不能被删除" : null);
    }

    @RequiresPermissions("配置管理:系统管理:公告管理")
    @RequestMapping(value = "/read-list", method = RequestMethod.GET)
    @ResponseBody
    public JsonPage<NotifyMessageRead> readList(HttpServletRequest request) {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(NotifyMessageRead.class, request);
        return notifyMessageReadService.findByPage(groupFilter, pageable);
    }

}
