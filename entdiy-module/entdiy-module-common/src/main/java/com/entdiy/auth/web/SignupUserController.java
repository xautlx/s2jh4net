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
package com.entdiy.auth.web;

import com.entdiy.auth.entity.SignupUser;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.SignupUserService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.EntityProcessCallbackHandler;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.support.service.DynamicConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/auth/signup-user")
public class SignupUserController extends BaseController<SignupUser, Long> {

    @Autowired
    private UserService userService;

    @Autowired
    private SignupUserService signupUserService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private RoleService roleService;

    @Override
    protected BaseService<SignupUser, Long> getEntityService() {
        return signupUserService;
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        Validation.notDemoMode(request);
        super.initPrepareModel(request, model, id);
    }

    @MenuData("配置管理:权限管理:注册用户管理")
    @RequiresPermissions("配置管理:权限管理:注册用户管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/auth/signupUser-index";
    }

    @RequiresPermissions("配置管理:权限管理:注册用户管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<SignupUser> findByPage(HttpServletRequest request) {
        return super.findByPage(SignupUser.class, request);
    }

    @RequiresPermissions("配置管理:权限管理:注册用户管理")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public String auditShow(Model model, @ModelAttribute("entity") SignupUser entity) {
        User user = new User();
        user.setMgmtGranted(true);
        //默认6个月后密码失效，到时用户登录强制要求重新设置密码
        user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
        entity.setUser(user);
        model.addAttribute("roles", roleService.findAllCached());
        return "admin/auth/signupUser-audit";
    }

    @RequiresPermissions("配置管理:权限管理:注册用户管理")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult auditSave(@ModelAttribute("entity") SignupUser entity, Model model) {

        signupUserService.auditNewUser(entity);
        return OperationResult.buildSuccessResult("数据保存处理完成", entity);
    }

    @Override
    @RequiresPermissions("配置管理:权限管理:注册用户管理")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids, new EntityProcessCallbackHandler<SignupUser>() {
            @Override
            public void processEntity(SignupUser entity) throws EntityProcessCallbackException {
                if (entity.getAuditTime() != null) {
                    throw new EntityProcessCallbackException("已审核数据不允许删除");
                }
            }
        });
    }
}
