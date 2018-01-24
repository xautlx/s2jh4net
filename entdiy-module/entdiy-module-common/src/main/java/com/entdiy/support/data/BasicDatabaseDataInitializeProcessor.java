/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.support.data;

import com.entdiy.auth.entity.*;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.Exceptions;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.sys.entity.ConfigProperty;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.entity.Menu;
import com.entdiy.sys.service.ConfigPropertyService;
import com.entdiy.sys.service.DataDictService;
import com.entdiy.sys.service.MenuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private PrivilegeService privilegeService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private DataDictService dataDictService;

    @Value("${base.packages}")
    private String basePackages;

    @Override
    public void initializeInternal() {
        logger.info("Running " + this.getClass().getName());
        LocalDateTime now = DateUtils.currentDateTime();

        //角色、用户等数据初始化,默认密码为:123456
        if (isEmptyTable(User.class)) {
            //后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限
            Role superRole = new Role();
            superRole.setCode(DefaultAuthUserDetails.ROLE_SUPER_USER);
            superRole.setName("后端超级管理员角色");
            superRole.setDescription("系统预置，请勿随意修改。后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限。");
            roleService.save(superRole);

            //预置超级管理员账号
            Account account = new Account();
            account.setAuthType(Account.AuthTypeEnum.admin);
            account.setAuthUid(GlobalConstant.ROOT_VALUE);
            account.setDataDomain(GlobalConstant.ROOT_VALUE);
            account.setEmail("xautlx@hotmail.com");
            accountService.save(account, "123456");

            User entity = new User();
            entity.setAccount(account);
            entity.setTrueName(GlobalConstant.ROOT_VALUE);
            //关联超级管理员角色
            UserR2Role r2 = new UserR2Role();
            r2.setUser(entity);
            r2.setRole(superRole);
            entity.setUserR2Roles(Lists.newArrayList(r2));
            userService.save(entity);

            //后端登录用户默认角色，具体权限可通过管理界面配置
            //所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role mgmtRole = new Role();
            mgmtRole.setCode(DefaultAuthUserDetails.ROLE_MGMT_USER);
            mgmtRole.setName("后端登录用户默认角色");
            mgmtRole.setDescription("系统预置，请勿随意修改。注意：所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据。");
            roleService.save(mgmtRole);

            entityManager.flush();
        }

        //权限数据初始化
        rebuildPrivilageDataFromControllerAnnotation();
        entityManager.flush();

        //菜单数据初始化
        rebuildMenuDataFromControllerAnnotation();
        entityManager.flush();

        if (configPropertyService.findByPropKey(GlobalConstant.CFG_SMS_DISABLED) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.CFG_SMS_DISABLED);
            entity.setPropName("是否全局禁用短信发送功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("紧急情况关闭短信发送接口服务");
            configPropertyService.save(entity);
            entityManager.flush();
        }

        //数据字典项初始化
        if (dataDictService.findByProperty("primaryKey", GlobalConstant.DATADICT_MESSAGE_TYPE) == null) {
            DataDict entity = new DataDict();
            entity.setPrimaryKey(GlobalConstant.DATADICT_MESSAGE_TYPE);
            entity.setPrimaryValue("消息类型");
            dataDictService.save(entity);
            entityManager.flush();
        }
    }

    /**
     * 基于Controller的@MenuData注解重建菜单基础数据
     */
    private void rebuildMenuDataFromControllerAnnotation() {
        try {
            logger.debug("Start to rebuildMenuDataFromControllerAnnotation...");
            LocalDateTime now = DateUtils.currentDateTime();

            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            for (String pkg : basePackages.split(",")) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }

            ClassPool pool = ClassPool.getDefault();
            //The default ClassPool returned by a static method ClassPool.getDefault() searches the same path that the underlying JVM (Java virtual machine) has. 
            //If a program is running on a web application server such as JBoss and Tomcat, 
            //the ClassPool object may not be able to find user classes since such a web application server uses multiple class loaders as well as the system class loader. 
            //In that case, an additional class path must be registered to the ClassPool. Suppose that pool refers to a ClassPool object:  
            pool.insertClassPath(new ClassClassPath(this.getClass()));

            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                CtClass cc = pool.get(className);
                CtMethod[] methods = cc.getMethods();
                for (CtMethod method : methods) {
                    MenuData menuData = (MenuData) method.getAnnotation(MenuData.class);
                    if (menuData != null) {
                        String[] paths = menuData.value();
                        Assert.isTrue(paths.length == 1, "Unimplments for multi menu path");
                        String fullpath = paths[0];
                        String[] names = fullpath.split(":");
                        for (int i = 0; i < names.length; i++) {
                            String path = StringUtils.join(names, ":", 0, i + 1);
                            Menu menu = menuService.findByProperty("path", path);
                            if (menu == null) {
                                menu = new Menu();
                                menu.setPath(path);
                                menu.setName(names[i]);
                                if (i > 0) {
                                    String parentPath = StringUtils.join(names, ":", 0, i);
                                    Menu parent = menuService.findByProperty("path", parentPath);
                                    menu.setParent(parent);
                                }
                            }
                            menu.setInheritLevel(i);

                            //计算菜单对应URL路径
                            if (i + 1 == names.length) {
                                String url = "";
                                RequestMapping clazzRequestMapping = (RequestMapping) cc.getAnnotation(RequestMapping.class);
                                if (clazzRequestMapping != null) {
                                    url = url + clazzRequestMapping.value()[0];
                                }
                                RequestMapping methodRequestMapping = (RequestMapping) method.getAnnotation(RequestMapping.class);
                                if (methodRequestMapping != null) {
                                    url = url + methodRequestMapping.value()[0];
                                }
                                menu.setUrl(url);
                                menu.setControllerClass(cc.getName());
                                menu.setControllerMethod(method.getName());
                            }

                            menu.setDisabled(menuData.disabled());
                            menu.setRebuildTime(now);
                            menuService.save(menu);
                        }
                    }
                }
            }

            //清理过期没用的菜单数据，倒序删除否则会有外键约束问题
            List<Menu> menus = menuService.findAllCached();
            for (int i = menus.size(); i > 0; i--) {
                Menu menu = menus.get(i - 1);
                if (menu.getRebuildTime().isBefore(now)) {
                    menuService.delete(menu);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 扫码Spring MVC Controller的所有方法的@RequiresPermissions注解，重建权限基础数据
     */
    private void rebuildPrivilageDataFromControllerAnnotation() {
        try {
            logger.debug("Start to rebuildPrivilageDataFromControllerAnnotation...");
            LocalDateTime now = DateUtils.currentDateTime();
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            for (String pkg : basePackages.split(",")) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }

            List<Privilege> privileges = privilegeService.findAllCached();
            ClassPool pool = ClassPool.getDefault();
            //The default ClassPool returned by a static method ClassPool.getDefault() searches the same path that the underlying JVM (Java virtual machine) has. 
            //If a program is running on a web application server such as JBoss and Tomcat, 
            //the ClassPool object may not be able to find user classes since such a web application server uses multiple class loaders as well as the system class loader. 
            //In that case, an additional class path must be registered to the ClassPool. Suppose that pool refers to a ClassPool object:  
            pool.insertClassPath(new ClassClassPath(this.getClass()));

            //合并所有类中所有RequiresPermissions定义信息
            Set<String> mergedPermissions = Sets.newHashSet();
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                CtClass cc = pool.get(className);
                CtMethod[] methods = cc.getMethods();
                for (CtMethod method : methods) {
                    RequiresPermissions rp = (RequiresPermissions) method.getAnnotation(RequiresPermissions.class);
                    if (rp != null) {
                        //int startLine = method.getMethodInfo().getLineNumber(0);
                        String[] perms = rp.value();
                        for (String perm : perms) {
                            mergedPermissions.add(perm);
                        }
                    }
                }
            }

            for (String perm : mergedPermissions) {
                Privilege entity = null;
                for (Privilege privilege : privileges) {
                    if (privilege.getCode().equals(perm)) {
                        entity = privilege;
                        break;
                    }
                }
                if (entity == null) {
                    entity = new Privilege();
                    entity.setCode(perm);
                }
                entity.setRebuildTime(now);
                privilegeService.save(entity);
            }

            //清理过期没用的权限数据
            for (Privilege privilege : privileges) {
                if (privilege.getRebuildTime().isBefore(now)) {
                    privilegeService.delete(privilege);
                }
            }
        } catch (Exception e) {
            Exceptions.unchecked(e);
        }
    }
}
