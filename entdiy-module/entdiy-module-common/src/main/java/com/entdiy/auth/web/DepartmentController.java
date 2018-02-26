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

import com.entdiy.auth.entity.Department;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
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
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/auth/department")
public class DepartmentController extends BaseController<Department, Long> {

    @Autowired
    private DepartmentService departmentService;

    @MenuData("配置管理:权限管理:部门配置")
    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity Department entity, Model model) {
        return "admin/auth/department-index";
    }


    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<Department> findByPage(@ModelPropertyFilter(Department.class) GroupPropertyFilter filter,
                                       @ModelPageableRequest Pageable pageable) {
        //如果没有业务查询参数，则限制只查询业务根节点数据
        if (filter.isEmptySearch()) {
            filter.forceAnd(new PropertyFilter(MatchType.EQ, "depth", 1));
        }
        return departmentService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue findTreeData(@ModelPropertyFilter(Department.class) GroupPropertyFilter filter,
                                            @ModelPageableRequest Pageable pageable,
                                            HttpServletRequest request) {
        filter.forceAnd(new PropertyFilter(MatchType.NE, "disabled", true));
        Object value = departmentService.findByPage(filter, pageable);
        final MappingJacksonValue jacksonValue = new MappingJacksonValue(value);
        jacksonValue.setSerializationView(PropertyFilter.parseJsonView(request, JsonViews.Tree.class));
        return jacksonValue;
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity Department entity, Model model) {
        return super.editSave(departmentService, entity);
    }

    @RequiresPermissions("配置管理:权限管理:部门配置")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(departmentService, ids);
    }
}
