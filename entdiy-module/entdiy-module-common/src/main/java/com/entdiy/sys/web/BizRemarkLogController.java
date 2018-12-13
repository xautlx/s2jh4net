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

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.service.Validation;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.BizRemarkLog;
import com.entdiy.sys.service.BizRemarkLogService;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@MetaData("业务备注记录")
@Controller
@RequestMapping(value = "/admin/sys/biz-remark-log")
public class BizRemarkLogController extends BaseController<BizRemarkLog, Long> {

    @Autowired
    private BizRemarkLogService bizRemarkLogService;

    @RequiresUser
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/sys/bizRemarkLog-index";
    }

    @RequiresUser
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonPage<BizRemarkLog> findByPage(
            @ModelPropertyFilter(value = BizRemarkLog.class, dataAccessControl = "dataDomain") GroupPropertyFilter filter,
            @ModelPageableRequest Pageable pageable) {
        return bizRemarkLogService.findByPage(filter, pageable);
    }

    @RequiresUser
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(@ModelEntity BizRemarkLog entity) {
        return "admin/sys/bizRemarkLog-inputBasic";
    }

    @RequiresUser
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity BizRemarkLog entity, HttpServletRequest request) {
        Validation.isTrue(entity.isNew(), "只允许新增数据");
        entity.setSubmitTime(LocalDateTime.now());
        return super.editSave(bizRemarkLogService, entity);
    }
}
