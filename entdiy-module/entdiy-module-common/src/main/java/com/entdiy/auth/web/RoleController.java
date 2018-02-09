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

import com.entdiy.auth.entity.Role;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin/auth/role")
public class RoleController extends BaseController<Role, Long> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @MenuData("配置管理:权限管理:角色配置")
    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity Role entity, Model model) {
        return "admin/auth/role-index";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<Role> findByPage(@ModelPropertyFilter(Role.class) GroupPropertyFilter filter,
                                 @ModelPageableRequest Pageable pageable) {
        return roleService.findByPage(filter, pageable);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(@ModelEntity Role entity, HttpServletRequest request) {
        return "admin/auth/role-inputTabs";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(@ModelEntity Role entity) {
        if (entity.isNew()) {
            entity.setCode("ROLE_");
        }
        return "admin/auth/role-inputBasic";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity Role entity, Model model) {
        return super.editSave(entity);
    }

    @Override
    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids);
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/privileges", method = RequestMethod.GET)
    public String privilegesShow(@ModelEntity(preFectchLazyFields = "roleR2Privileges") Role entity, Model model) {
        Set<Long> r2PrivilegeIds = Sets.newHashSet();
        List<RoleR2Privilege> r2s = entity.getRoleR2Privileges();
        if (CollectionUtils.isNotEmpty(r2s)) {
            for (RoleR2Privilege r2 : r2s) {
                r2PrivilegeIds.add(r2.getPrivilege().getId());
            }
        }
        model.addAttribute("r2PrivilegeIds", StringUtils.join(r2PrivilegeIds, ","));
        return "admin/auth/role-privileges";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/privileges", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult privilegesSave(@ModelEntity(preFectchLazyFields = "roleR2Privileges") Role entity, Model model) {
        return super.editSave(entity);
    }
}
