package lab.s2jh.support.data;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.cons.GlobalConstant;
import lab.s2jh.core.context.ExtPropertyPlaceholderConfigurer;
import lab.s2jh.core.data.BaseDatabaseDataInitialize;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.core.util.UidUtils;
import lab.s2jh.module.auth.entity.Department;
import lab.s2jh.module.auth.entity.Privilege;
import lab.s2jh.module.auth.entity.Role;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.module.auth.entity.UserR2Role;
import lab.s2jh.module.auth.service.DepartmentService;
import lab.s2jh.module.auth.service.PrivilegeService;
import lab.s2jh.module.auth.service.RoleService;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.entity.DataDict;
import lab.s2jh.module.sys.entity.Menu;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.module.sys.service.ConfigPropertyService;
import lab.s2jh.module.sys.service.DataDictService;
import lab.s2jh.module.sys.service.MenuService;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.module.sys.service.UserMessageService;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 数据库基础数据初始化处理器
 */
@Component
public class BasicDatabaseDataInitialize extends BaseDatabaseDataInitialize {

    private static final Logger logger = LoggerFactory.getLogger(BasicDatabaseDataInitialize.class);

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

        //角色、用户等数据初始化,默认密码为:账号+123
        if (isEmptyTable(User.class)) {
            //后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限
            Role superRole = new Role();
            superRole.setCode(AuthUserDetails.ROLE_SUPER_USER);
            superRole.setName("后端超级管理员角色");
            superRole.setDescription("系统预置，请勿随意修改");
            roleService.save(superRole);

            //预置超级管理员账号
            User entity = new User();
            entity.setAuthUid("admin");
            entity.setAuthType(AuthTypeEnum.SYS);
            entity.setMgmtGranted(true);
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
            mgmtRole.setDescription("系统预置，请勿随意修改");
            roleService.save(mgmtRole);

            //后台默认普通管理员账号
            entity = new User();
            entity.setAuthGuid(UidUtils.UID());
            entity.setAuthUid("mgmt");
            entity.setAuthType(AuthTypeEnum.SYS);
            entity.setMgmtGranted(true);
            entity.setNickName("后台默认普通管理员");
            //默认密码失效，用户初始密码登录后则强制修改密码
            entity.setCredentialsExpireTime(now);
            userService.save(entity, entity.getAuthUid() + "123");

            //前端登录用户默认角色，，具体权限可通过管理界面配置
            //所有前端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role siteUserRole = new Role();
            siteUserRole.setCode(AuthUserDetails.ROLE_SITE_USER);
            siteUserRole.setName("前端登录用户默认角色");
            siteUserRole.setDescription("系统预置，请勿随意修改");
            roleService.save(siteUserRole);

            if (DynamicConfigService.isDemoMode()) {
                Department department = new Department();
                department.setCode("SC00");
                department.setName("市场部");
                departmentService.save(department);

                Department department1 = new Department();
                department1.setCode("SC01");
                department1.setName("市场一部");
                department1.setParent(department);
                departmentService.save(department1);

                Department department2 = new Department();
                department2.setCode("SC02");
                department2.setName("市场二部");
                department2.setParent(department);
                departmentService.save(department2);
            }
        }

        //权限数据初始化
        rebuildPrivilageDataFromControllerAnnotation();

        //菜单数据初始化
        rebuildMenuDataFromControllerAnnotation();

        //属性文件中配置的系统名称
        String systemTitle = "未定义";
        if (extPropertyPlaceholderConfigurer != null) {
            systemTitle = extPropertyPlaceholderConfigurer.getProperty("cfg_system_title");
        }

        //系统配置参数初始化
        if (isEmptyTable(ConfigProperty.class)) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey("cfg_system_title");
            entity.setPropName("系统名称");
            entity.setSimpleValue(systemTitle);
            configPropertyService.save(entity);

            entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.cfg_signup_disabled);
            entity.setPropName("禁用自助注册功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("设置为true禁用则登录界面屏蔽自助注册功能");
            configPropertyService.save(entity);

            entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.cfg_public_send_sms_disabled);
            entity.setPropName("是否全局禁用开放手机号短信发送功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("如果为true则只会向已在平台验证通过的手机号发送短信，其他在平台从未验证过的手机号不再发送短信");
            configPropertyService.save(entity);
        }

        {
            if (dataDictService.findByProperty("primaryKey", GlobalConstant.DataDict_Message_Type) == null) {
                DataDict entity = new DataDict();
                entity.setPrimaryKey(GlobalConstant.DataDict_Message_Type);
                entity.setPrimaryValue("消息类型");
                dataDictService.save(entity);

                DataDict item = new DataDict();
                item.setPrimaryKey("notify");
                item.setPrimaryValue("通知");
                item.setSecondaryValue("#32CFC4");
                item.setParent(entity);
                dataDictService.save(item);

                item = new DataDict();
                item.setPrimaryKey("bulletin");
                item.setPrimaryValue("喜报");
                item.setSecondaryValue("#FF645D");
                item.setParent(entity);
                dataDictService.save(item);

                item = new DataDict();
                item.setPrimaryKey("remind");
                item.setPrimaryValue("提醒");
                item.setSecondaryValue("#FF8524");
                item.setParent(entity);
                dataDictService.save(item);
            }
        }

        //初始化演示通知消息
        if (isEmptyTable(NotifyMessage.class)) {
            NotifyMessage entity = new NotifyMessage();
            entity.setType("notify");
            entity.setTitle("欢迎访问" + systemTitle);
            entity.setPublishTime(now);
            entity.setMessage("<p>系统初始化时间：" + DateUtils.formatTime(now) + "</p>");
            notifyMessageService.save(entity);
        }

        //初始化演示通知消息
        if (isEmptyTable(UserMessage.class)) {
            User admin = userService.findByAuthUid("admin");

            UserMessage entity = new UserMessage();
            entity.setType("notify");
            entity.setPublishTime(DateUtils.currentDate());
            entity.setTitle("演示个人消息1");
            entity.setTargetUser(admin);
            entity.setMessage("<p>演示定向发送个人消息1内容</p>");
            userMessageService.save(entity);

            entity = new UserMessage();
            entity.setType("bulletin");
            entity.setPublishTime(DateUtils.currentDate());
            entity.setTitle("演示个人消息2");
            entity.setTargetUser(admin);
            entity.setMessage("<p>演示定向发送个人消息2内容</p>");
            userMessageService.save(entity);
        }
    }

    /**
     * 基于Controller的@MenuData注解重建菜单基础数据
     */
    private void rebuildMenuDataFromControllerAnnotation() {
        try {
            Date now = DateUtils.currentDate();

            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**.web.**"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**.web.**"));

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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 扫码Spring MVC Controller的所有方法的@RequiresPermissions注解，重建权限基础数据
     */
    private void rebuildPrivilageDataFromControllerAnnotation() {
        try {
            Date now = DateUtils.currentDate();
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**.web.**"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**.web.**"));

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
        } catch (Exception e) {
            Exceptions.unchecked(e);
        }
    }
}
