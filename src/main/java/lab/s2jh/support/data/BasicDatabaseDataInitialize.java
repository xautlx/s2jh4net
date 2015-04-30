package lab.s2jh.support.data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.context.ExtPropertyPlaceholderConfigurer;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.security.PasswordService;
import lab.s2jh.core.service.GlobalConfigService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.entity.Department;
import lab.s2jh.module.auth.entity.Privilege;
import lab.s2jh.module.auth.entity.Role;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.module.auth.entity.UserR2Role;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.entity.Menu;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.UserMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

/**
 * 数据库基础数据初始化处理器
 */
@Component
public class BasicDatabaseDataInitialize {

    private static final Logger logger = LoggerFactory.getLogger(BasicDatabaseDataInitialize.class);

    @Autowired
    @Qualifier("entityManagerFactoryApp")
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ExtPropertyPlaceholderConfigurer extPropertyPlaceholderConfigurer;

    @PostConstruct
    public void initialize() throws Exception {

        logger.info("Running " + this.getClass().getName());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Date now = new Date();

        entityManager.getTransaction().begin();

        //搜索所有entity对象，并自动进行自增初始化值设置
        ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
        scan.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        Set<BeanDefinition> beanDefinitions = scan.findCandidateComponents("**.entity.**");
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
            MetaData metaData = entityClass.getAnnotation(MetaData.class);
            if (metaData != null && metaData.autoIncrementInitValue() > 0) {
                MockEntityUtils.autoIncrementInitValue(entityClass, entityManager);
            }
        }

        //角色、用户等数据初始化,默认密码为:账号+123
        if (MockEntityUtils.isEmptyTable(User.class, entityManager)) {
            //后端预置超级管理员，无需配置相关权限，默认自动赋予所有权限
            Role superRole = new Role();
            superRole.setCode(AuthUserDetails.ROLE_SUPER_USER);
            superRole.setName("后端超级管理员角色");
            superRole.setDescription("系统预置，请勿随意修改");
            entityManager.persist(superRole);
            //预置超级管理员账号
            User entity = new User();
            entity.setAuthGuid(UUID.randomUUID().toString());
            entity.setAuthUid("admin");
            entity.setAuthType(AuthTypeEnum.SYS);
            entity.setMgmtGranted(true);
            entity.setNickName("后端预置超级管理员");
            entity.setSignupTime(now);
            entity.setPassword(passwordService.entryptPassword(entity.getAuthUid() + "123", entity.getAuthGuid()));
            entityManager.persist(entity);
            //关联超级管理员角色
            UserR2Role r2 = new UserR2Role();
            r2.setUser(entity);
            r2.setRole(superRole);
            entityManager.persist(r2);

            //后端登录用户默认角色，具体权限可通过管理界面配置
            //所有后端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role mgmtRole = new Role();
            mgmtRole.setCode(AuthUserDetails.ROLE_MGMT_USER);
            mgmtRole.setName("后端登录用户默认角色");
            mgmtRole.setDescription("系统预置，请勿随意修改");
            entityManager.persist(mgmtRole);
            //后台默认普通管理员账号
            entity = new User();
            entity.setAuthGuid(UUID.randomUUID().toString());
            entity.setAuthUid("mgmt");
            entity.setAuthType(AuthTypeEnum.SYS);
            entity.setMgmtGranted(true);
            entity.setNickName("后台默认普通管理员");
            entity.setSignupTime(now);
            entity.setPassword(passwordService.entryptPassword(entity.getAuthUid() + "123", entity.getAuthGuid()));
            entityManager.persist(entity);

            //前端登录用户默认角色，，具体权限可通过管理界面配置
            //所有前端登录用户默认关联此角色，无需额外写入用户和角色关联数据
            Role siteUserRole = new Role();
            siteUserRole.setCode(AuthUserDetails.ROLE_SITE_USER);
            siteUserRole.setName("前端登录用户默认角色");
            siteUserRole.setDescription("系统预置，请勿随意修改");
            entityManager.persist(siteUserRole);

            if (GlobalConfigService.isDevMode()) {
                Department department = new Department();
                department.setCode("SC00");
                department.setName("市场部");
                entityManager.persist(department);

                Department department1 = new Department();
                department1.setCode("SC01");
                department1.setName("市场一部");
                department1.setParent(department);
                entityManager.persist(department1);

                Department department2 = new Department();
                department2.setCode("SC02");
                department2.setName("市场二部");
                department2.setParent(department);
                entityManager.persist(department2);
            }
        } else {
            //如果不是开发模式，则直接退出防止意外更新已有数据
            //为了稳妥，生产环境数据采用手工更新方式
            if (!GlobalConfigService.isDevMode()) {
                return;
            }
        }

        //权限数据初始化
        rebuildPrivilageDataFromControllerAnnotation(entityManager);

        //菜单数据初始化
        rebuildMenuDataFromControllerAnnotation(entityManager);

        //属性文件中配置的系统名称
        String systemTitle = "未定义";
        if (extPropertyPlaceholderConfigurer != null) {
            systemTitle = extPropertyPlaceholderConfigurer.getProperty("cfg_system_title");
        }

        //系统配置参数初始化
        if (MockEntityUtils.isEmptyTable(ConfigProperty.class, entityManager)) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey("cfg_system_title");
            entity.setPropName("系统名称");
            entity.setSimpleValue(systemTitle);
            entityManager.persist(entity);

            entity = new ConfigProperty();
            entity.setPropKey("cfg_signup_disabled");
            entity.setPropName("禁用自助注册功能");
            entity.setSimpleValue("false");
            entity.setPropDescn("设置为true禁用则登录界面屏蔽自助注册功能");
            entityManager.persist(entity);
        }

        //初始化演示通知消息
        if (MockEntityUtils.isEmptyTable(NotifyMessage.class, entityManager)) {
            NotifyMessage entity = new NotifyMessage();
            entity.setTitle("欢迎访问" + systemTitle);
            entity.setPublishTime(now);
            entity.setEffective(true);
            entity.setMessage("<p>系统初始化时间：" + DateUtils.formatTime(now) + "</p>");
            entityManager.persist(entity);
        }

        //初始化演示通知消息
        if (MockEntityUtils.isEmptyTable(UserMessage.class, entityManager)) {
            User admin = (User) entityManager.createQuery("from User where authUid='admin'").getSingleResult();

            UserMessage entity = new UserMessage();
            entity.setTitle("演示个人消息1");
            entity.setPublishTime(now);
            entity.setEffective(true);
            entity.setTargetUser(admin);
            entity.setMessage("<p>演示定向发送个人消息1内容</p>");
            entityManager.persist(entity);

            entity = new UserMessage();
            entity.setTitle("演示个人消息2");
            entity.setPublishTime(now);
            entity.setEffective(true);
            entity.setTargetUser(admin);
            entity.setMessage("<p>演示定向发送个人消息2内容</p>");
            entityManager.persist(entity);
        }

        entityManager.flush();
        entityManager.close();

        entityManager.getTransaction().commit();
    }

    /**
     * 基于Controller的@MenuData注解重建菜单基础数据
     */
    private void rebuildMenuDataFromControllerAnnotation(EntityManager entityManager) {
        try {
            Date now = new Date();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            //扫码所有代码
            Set<BeanDefinition> beanDefinitions = scan.findCandidateComponents("");
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
                            Query query = entityManager.createQuery("from Menu where path=:path");
                            query.setParameter("path", path);
                            Menu menu = null;
                            if (CollectionUtils.isNotEmpty(query.getResultList())) {
                                menu = (Menu) query.getResultList().get(0);
                            }

                            if (menu == null) {
                                menu = new Menu();
                                menu.setPath(path);
                                menu.setName(names[i]);
                                if (i > 0) {
                                    String parentPath = StringUtils.join(names, ":", 0, i);
                                    Menu parent = (Menu) entityManager.createQuery("from Menu where path=:path").setParameter("path", parentPath)
                                            .getSingleResult();
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
                            entityManager.merge(menu);
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
    private void rebuildPrivilageDataFromControllerAnnotation(EntityManager entityManager) {
        try {
            Date now = new Date();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            Set<BeanDefinition> beanDefinitions = scan.findCandidateComponents("");
            @SuppressWarnings("unchecked")
            List<Privilege> privileges = entityManager.createQuery("from Privilege").getResultList();
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
                entityManager.merge(entity);
            }
        } catch (Exception e) {
            Exceptions.unchecked(e);
        }
    }
}
