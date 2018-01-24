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

import com.entdiy.auth.entity.Role;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.view.OperationResult;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Override
    protected BaseService<Role, Long> getEntityService() {
        return roleService;
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        Validation.notDemoMode(request);
        Role role = super.initPrepareModel(request, model, id);
        if (role.isNew()) {
            role.setCode("ROLE_");
        }
    }

    @Override
    protected Role buildDetachedBindingEntity(Long id) {
        return roleService.findDetachedOne(id, "roleR2Privileges");
    }

    @MenuData("配置管理:权限管理:角色配置")
    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/auth/role-index";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<Role> findByPage(HttpServletRequest request) {
        return super.findByPage(Role.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/auth/role-inputTabs";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow() {
        return "admin/auth/role-inputBasic";
    }

    @RequiresPermissions("配置管理:权限管理:角色配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") Role entity, Model model) {
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
    public String privilegeR2sShow(@ModelAttribute("entity") Role entity, Model model) {
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
    public OperationResult privilegeR2sSave(@ModelAttribute("entity") Role entity,
                                            @RequestParam(value = "privilegeIds", required = false) Long[] privilegeIds) {
        roleService.updateRelatedPrivilegeR2s(entity, privilegeIds);
        return OperationResult.buildSuccessResult();
    }

}
