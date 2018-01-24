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
package com.entdiy.auth.web;

import com.entdiy.auth.entity.Department;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.view.OperationResult;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/auth/department")
public class DepartmentController extends BaseController<Department, Long> {

    @Autowired
    private DepartmentService departmentService;

    @Override
    protected BaseService<Department, Long> getEntityService() {
        return departmentService;
    }

    @MenuData("配置管理:权限管理:部门配置")
    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/auth/department-index";
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmptySearch()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<Department> findByPage(HttpServletRequest request) {
        return super.findByPage(Department.class, request);
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue findTreeData(HttpServletRequest request) {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(Department.class, request);
        groupFilter.forceAnd(new PropertyFilter(MatchType.NE, "disabled", true));
        Object value = getEntityService().findByPage(groupFilter, pageable);
        final MappingJacksonValue jacksonValue = new MappingJacksonValue(value);
        jacksonValue.setSerializationView(PropertyFilter.parseJsonView(request, JsonViews.Admin.class));
        return jacksonValue;
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") Department entity, Model model) {
        return super.editSave(entity);
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids);
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }
}
