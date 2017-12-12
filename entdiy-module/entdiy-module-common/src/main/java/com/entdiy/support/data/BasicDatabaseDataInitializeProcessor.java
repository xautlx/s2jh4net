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
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.ExtPropertyPlaceholderConfigurer;
import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.Exceptions;
import com.entdiy.security.AuthUserDetails;
import com.entdiy.sys.entity.*;
import com.entdiy.sys.service.*;
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 数据库基础数据初始化处理器
 */
@Component
public class BasicDatabaseDataInitializeProcessor extends AbstractDatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseDataInitializeProcessor.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private ExtPropertyPlaceholderConfigurer extPropertyPlaceholderConfigurer;

    @Override
    public void initializeInternal() {

        logger.info("Running " + this.getClass().getName());
        Date now = DateUtils.currentDate();

        //初始化演示部门数据
        if (isEmptyTable(Department.class)) {

            Department rootDepartment = new Department();
            rootDepartment.setCode("ROOT");
            rootDepartment.setName("总部");
            departmentService.save(rootDepartment);

            if (GlobalConfigService.isDemoMode() || GlobalConfigService.isDevMode()) {
                Department department10 = new Department();
                department10.setCode("SC00");
                department10.setName("市场部");
                departmentService.save(department10);

                Department department11 = new Department();
                department11.setCode("SC01");
                department11.setName("市场一部");
                department11.setParent(department10);
                departmentService.save(department11);

                Department department12 = new Department();
                department12.setCode("SC02");
                department12.setName("市场二部");
                department12.setParent(department10);
                departmentService.save(department12);

                Department department20 = new Department();
                department20.setCode("YF00");
                department20.setName("研发部");
                departmentService.save(department20);

                Department department21 = new Department();
                department21.setCode("YF01");
                department21.setName("研发一部");
                department21.setParent(department20);
                departmentService.save(department21);

                Department department22 = new Department();
                department22.setCode("YF02");
                department22.setName("研发二部");
                department22.setParent(department20);
                departmentService.save(department22);
            }
        }

        //角色、用户等数据初始化,默认密码为:账号+123
        if (isEmptyTable(User.class)) {
            //后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限
            Role superRole = new Role();
            superRole.setCode(AuthUserDetails.ROLE_SUPER_USER);
            superRole.setName("后端超级管理员角色");
            superRole.setDescription("系统预置，请勿随意修改。后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限。");
            roleService.save(superRole);

            //预置超级管理员账号
            User entity = new User();
            entity.setAuthUid("admin");
            entity.setAuthType(AuthTypeEnum.SYS);
            entity.setDepartment(departmentService.findByProperty("code", "ROOT"));
            entity.setMgmtGranted(true);
            entity.setEmail("xautlx@hotmail.com");
            entity.setNickName("后端预置超级管理员");
            //关联超级管理员角色
            UserR2Role r2 = new UserR2Role();
            r2.setUser(entity);
            r2.setRole(superRole);
            entity.setUserR2Roles(Lists.newArrayList(r2));
            userService.save(entity, entity.getAuthUid() + "123");

            //后端登录用户默认角色，具体权限可通过管理界面配置
            //所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role mgmtRole = new Role();
            mgmtRole.setCode(AuthUserDetails.ROLE_MGMT_USER);
            mgmtRole.setName("后端登录用户默认角色");
            mgmtRole.setDescription("系统预置，请勿随意修改。注意：所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据。");
            roleService.save(mgmtRole);

            //前端登录用户默认角色，，具体权限可通过管理界面配置
            //所有前端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role siteUserRole = new Role();
            siteUserRole.setCode(AuthUserDetails.ROLE_SITE_USER);
            siteUserRole.setName("前端登录用户默认角色");
            siteUserRole.setDescription("系统预置，请勿随意修改。注意：所有前端登录用户默认关联此角色，无需额外写入用户和角色关联数据。");
            roleService.save(siteUserRole);
        }

        //权限数据初始化
        rebuildPrivilageDataFromControllerAnnotation();
        commitAndResumeTransaction();

        //菜单数据初始化
        rebuildMenuDataFromControllerAnnotation();
        commitAndResumeTransaction();

        //属性文件中配置的系统名称
        String systemTitle = "未定义";
        if (extPropertyPlaceholderConfigurer != null) {
            systemTitle = extPropertyPlaceholderConfigurer.getProperty("cfg_system_title");
        }

        //系统配置参数初始化
        if (configPropertyService.findByPropKey(GlobalConstant.cfg_system_title) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.cfg_system_title);
            entity.setPropName("系统名称");
            entity.setSimpleValue(systemTitle);
            configPropertyService.save(entity);
        }
        if (configPropertyService.findByPropKey(GlobalConstant.cfg_mgmt_signup_disabled) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.cfg_mgmt_signup_disabled);
            entity.setPropName("禁用自助注册功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("设置为true禁用则登录界面屏蔽自助注册功能");
            configPropertyService.save(entity);
        }
        if (configPropertyService.findByPropKey(GlobalConstant.cfg_public_send_sms_disabled) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.cfg_public_send_sms_disabled);
            entity.setPropName("是否全局禁用开放手机号短信发送功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("如果为true则只会向已在平台验证通过的手机号发送短信，其他在平台从未验证过的手机号不再发送短信");
            configPropertyService.save(entity);
        }

        //数据字典项初始化
        if (dataDictService.findByProperty("primaryKey", GlobalConstant.DataDict_Message_Type) == null) {
            DataDict entity = new DataDict();
            entity.setPrimaryKey(GlobalConstant.DataDict_Message_Type);
            entity.setPrimaryValue("消息类型");
            dataDictService.save(entity);

            DataDict item = new DataDict();
            item.setPrimaryKey("normal");
            item.setPrimaryValue("一般通知");
            item.setSecondaryValue("#32CFC4");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("important");
            item.setPrimaryValue("重要通知");
            item.setSecondaryValue("#FF645D");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("urgent");
            item.setPrimaryValue("紧急通知");
            item.setSecondaryValue("#FF0000");
            item.setParent(entity);
            dataDictService.save(item);
        }

        if (GlobalConfigService.isDemoMode() || GlobalConfigService.isDevMode()) {
            //初始化演示通知消息
            if (isEmptyTable(NotifyMessage.class)) {
                NotifyMessage entity = new NotifyMessage();
                entity.setType("normal");
                entity.setTitle("欢迎访问" + systemTitle);
                entity.setPublishTime(now);
                entity.setMessage("<p>系统初始化时间：" + DateUtils.formatTime(now) + "</p>");
                notifyMessageService.save(entity);

                entity = new NotifyMessage();
                entity.setType("important");
                entity.setTitle("版本更新通知");
                entity.setPublishTime(now);
                entity.setMessage("<p>整体重构项目Maven结构，模块化拆分，使定制开发能按需所取</p><p>UI基础框架版本从 Metronic 1.4.5 升级到 4.7.5</p>");
                notifyMessageService.save(entity);

                entity = new NotifyMessage();
                entity.setType("urgent");
                entity.setTitle("系统更新维护通知");
                entity.setPublishTime(now);
                entity.setMessage("<p>计划在XX进行系统迁移升级，届时本系统不可用，预计一小时迁移完成恢复使用</p>");
                notifyMessageService.save(entity);
            }

            //初始化演示通知消息
            if (isEmptyTable(UserMessage.class)) {
                User admin = userService.findByAuthUid("admin");

                UserMessage entity = new UserMessage();
                entity.setType("normal");
                entity.setPublishTime(DateUtils.currentDate());
                entity.setTitle("演示个人消息1");
                entity.setTargetUser(admin);
                entity.setMessage("<p>演示定向发送个人消息1内容</p>");
                userMessageService.save(entity);

                entity = new UserMessage();
                entity.setType("important");
                entity.setPublishTime(DateUtils.currentDate());
                entity.setTitle("演示个人消息2");
                entity.setTargetUser(admin);
                entity.setMessage("<p>演示定向发送个人消息2内容</p>");
                userMessageService.save(entity);
            }
        }
    }

    /**
     * 基于Controller的@MenuData注解重建菜单基础数据
     */
    private void rebuildMenuDataFromControllerAnnotation() {
        try {
            logger.debug("Start to rebuildMenuDataFromControllerAnnotation...");
            Date now = DateUtils.currentDate();

            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            String[] packages = StringUtils.split(ExtPropertyPlaceholderConfigurer.getBasePackages(),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
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
                if (menu.getRebuildTime().before(now)) {
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
            Date now = DateUtils.currentDate();
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            String[] packages = StringUtils.split(ExtPropertyPlaceholderConfigurer.getBasePackages(),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
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
                if (privilege.getRebuildTime().before(now)) {
                    privilegeService.delete(privilege);
                }
            }
        } catch (Exception e) {
            Exceptions.unchecked(e);
        }
    }
}
