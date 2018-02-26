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
package com.entdiy.support.data;

import com.entdiy.auth.entity.*;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.sys.entity.ConfigProperty;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.entity.Menu;
import com.entdiy.sys.service.ConfigPropertyService;
import com.entdiy.sys.service.DataDictService;
import com.entdiy.sys.service.MenuService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据库基础数据初始化处理器
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BasicDatabaseDataInitializeProcessor extends AbstractDatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseDataInitializeProcessor.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MenuService menuService;

    @Override
    public void initializeInternal() {
        logger.info("Running " + this.getClass().getName());

        //角色、用户等数据初始化,默认密码为:123456
        if (isEmptyTable(User.class)) {
            //后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限
            Role superRole = new Role();
            superRole.setCode(DefaultAuthUserDetails.ROLE_SUPER_USER);
            superRole.setName("后端超级管理员角色");
            superRole.setDescription("系统预置，请勿随意修改。后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限。");
            roleService.save(superRole);

            //预置超级管理员账号
            Account rootAccount = new Account();
            rootAccount.setAuthType(Account.AuthTypeEnum.admin);
            rootAccount.setAuthUid(GlobalConstant.ROOT_VALUE);
            rootAccount.setDataDomain(GlobalConstant.DEFAULT_VALUE);
            rootAccount.setEmail("xautlx@hotmail.com");

            User rootUser = new User();
            rootUser.setAccount(rootAccount);
            rootUser.setTrueName(rootAccount.getAuthUid());
            //关联超级管理员角色
            UserR2Role r2 = new UserR2Role();
            r2.setUser(rootUser);
            r2.setRole(superRole);
            rootUser.setUserR2Roles(Lists.newArrayList(r2));
            userService.saveCascadeAccount(rootUser, "123456");

            //后端登录用户默认角色，具体权限可通过管理界面配置
            //所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role mgmtRole = new Role();
            mgmtRole.setCode(DefaultAuthUserDetails.ROLE_MGMT_USER);
            mgmtRole.setName("后端登录用户默认角色");
            mgmtRole.setDescription("系统预置，请勿随意修改。注意：所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据。");
            roleService.save(mgmtRole);

            //预置普通管理账号
            Account userAccount = new Account();
            userAccount.setAuthType(Account.AuthTypeEnum.admin);
            userAccount.setAuthUid("user");
            userAccount.setDataDomain(GlobalConstant.DEFAULT_VALUE);
            userAccount.setEmail("user@entdiy.com");

            User user = new User();
            user.setAccount(userAccount);
            user.setTrueName(userAccount.getAuthUid());
            userService.saveCascadeAccount(user, "123456");
        }

        //初始化Nested Set Model默认根节点数据
        if (isEmptyTable(Department.class)) {
            Department root = new Department();
            root.setName(GlobalConstant.ROOT_VALUE);
            root.setCode(GlobalConstant.ROOT_VALUE);
            departmentService.save(root);
        }

        //初始化Nested Set Model默认根节点数据
        if (isEmptyTable(Menu.class)) {
            Menu root = new Menu();
            root.setName(GlobalConstant.ROOT_VALUE);
            root.setPath(GlobalConstant.ROOT_VALUE);
            menuService.save(root);
        }

        //初始化Nested Set Model默认根节点数据
        if (isEmptyTable(DataDict.class)) {
            DataDict root = new DataDict();
            root.setPrimaryKey(GlobalConstant.ROOT_VALUE);
            root.setPrimaryValue(GlobalConstant.ROOT_VALUE);
            dataDictService.save(root);
        }

        if (!configPropertyService.findByPropKey(GlobalConstant.CFG_SMS_DISABLED).isPresent()) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.CFG_SMS_DISABLED);
            entity.setPropName("是否全局禁用短信发送功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("紧急情况关闭短信发送接口服务");
            configPropertyService.save(entity);
        }

        //数据字典项初始化
        if (!dataDictService.findByRootPrimaryKey(GlobalConstant.DATADICT_MESSAGE_TYPE).isPresent()) {
            DataDict entity = new DataDict();
            entity.setPrimaryKey(GlobalConstant.DATADICT_MESSAGE_TYPE);
            entity.setPrimaryValue("消息类型");
            entity.setParent(dataDictService.findRoot());
            dataDictService.save(entity);
        }
    }
}
