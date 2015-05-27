package lab.s2jh.module.sys.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.security.ShiroJdbcRealm;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.module.auth.entity.Role;
import lab.s2jh.module.auth.entity.RoleR2Privilege;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.UserR2Role;
import lab.s2jh.module.sys.dao.MenuDao;
import lab.s2jh.module.sys.entity.Menu;
import lab.s2jh.module.sys.vo.NavMenuVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service
@Transactional
public class MenuService extends BaseService<Menu, Long> {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuDao menuDao;

    @Autowired(required = false)
    private ShiroJdbcRealm shiroJdbcRealm;

    @Autowired(required = false)
    private AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor;

    private static Map<Long, MethodInvocation> cachedMethodInvocations;

    @Override
    protected BaseDao<Menu, Long> getEntityDao() {
        return menuDao;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAllCached() {
        return menuDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public List<NavMenuVO> findAvailableNavMenuVOs() {
        List<Menu> allMenus = findAllCached();
        Set<String> disabledMenuPaths = Sets.newHashSet();
        for (Menu menu : allMenus) {
            if (Boolean.TRUE.equals(menu.getDisabled())) {
                disabledMenuPaths.add(menu.getPath());
            }
        }
        List<Menu> availableMenus = Lists.newArrayList();
        for (Menu menu : allMenus) {
            if (!Boolean.TRUE.equals(menu.getDisabled())) {
                String path = menu.getPath();
                boolean disabled = false;
                if (CollectionUtils.isNotEmpty(disabledMenuPaths)) {
                    for (String disabledMenuPath : disabledMenuPaths) {
                        if (path.startsWith(disabledMenuPath)) {
                            disabled = true;
                            break;
                        }
                    }
                }
                if (!disabled) {
                    availableMenus.add(menu);
                }
            }
        }

        List<NavMenuVO> navMenuVOs = Lists.newArrayList();
        for (Menu menu : availableMenus) {
            NavMenuVO vo = new NavMenuVO();
            navMenuVOs.add(vo);
            vo.setId(menu.getId());
            vo.setName(menu.getName());
            vo.setPath(menu.getPath());
            vo.setStyle(menu.getStyle());
            vo.setUrl(menu.getUrl());
            vo.setInitOpen(menu.getInitOpen());
            if (menu.getParent() != null) {
                vo.setParentId(menu.getParent().getId());
            }
        }

        return navMenuVOs;
    }

    public MethodInvocation getMappedMethodInvocation(NavMenuVO navMenuVO) {
        //初始化用于shiro权限比对检查判断的MethodInvocation对应缓存数据
        if (cachedMethodInvocations == null) {
            cachedMethodInvocations = Maps.newHashMap();
            List<Menu> allMenus = findAllCached();
            for (Menu menu : allMenus) {
                //基于记录的Controller类和方法信息构造MethodInvocation,用于后续调用shiro的拦截器进行访问权限比对
                if (StringUtils.isNotBlank(menu.getControllerMethod())) {

                    try {
                        final Class<?> clazz = ClassUtils.getClass(menu.getControllerClass());
                        Method[] methods = clazz.getMethods();
                        for (final Method method : methods) {
                            if (method.getName().equals(menu.getControllerMethod())) {
                                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                                if (rm.method() == null || rm.method().length == 0 || ArrayUtils.contains(rm.method(), RequestMethod.GET)) {
                                    cachedMethodInvocations.put(menu.getId(), new MethodInvocation() {

                                        @Override
                                        public Object proceed() throws Throwable {
                                            return null;
                                        }

                                        @Override
                                        public Object getThis() {
                                            try {
                                                return clazz.newInstance();
                                            } catch (Exception e) {
                                                Exceptions.unchecked(e);
                                            }
                                            return null;
                                        }

                                        @Override
                                        public Method getMethod() {
                                            return method;
                                        }

                                        @Override
                                        public Object[] getArguments() {
                                            return null;
                                        }
                                    });
                                    break;
                                }
                            }
                        }

                    } catch (Exception e) {
                        Exceptions.unchecked(e);
                    }
                }
            }
        }
        return cachedMethodInvocations.get(navMenuVO.getId());
    }

    /**
     * 计算用户的有权限的菜单列表
     * @return
     */
    public List<NavMenuVO> processUserMenu(User user) {
        //获取所有有效的菜单集合
        List<NavMenuVO> navMenuVOs = findAvailableNavMenuVOs();
        List<UserR2Role> userR2Roles = user.getUserR2Roles();
        Set<String> userRoles = Sets.newHashSet();
        Set<String> userPrivileges = Sets.newHashSet();
        for (UserR2Role userR2Role : userR2Roles) {
            Role role = userR2Role.getRole();
            //如果是超级管理员直接返回所有有效菜单
            if (role.getCode().equals(AuthUserDetails.ROLE_SUPER_USER)) {
                return navMenuVOs;
            }
            userRoles.add(role.getCode());
            List<RoleR2Privilege> roleR2Privileges = role.getRoleR2Privileges();
            for (RoleR2Privilege roleR2Privilege : roleR2Privileges) {
                userPrivileges.add(roleR2Privilege.getPrivilege().getCode());
            }
        }

        List<NavMenuVO> userNavMenuVOs = Lists.newArrayList();

        //计算用户有访问权限的菜单列表
        for (NavMenuVO navMenuVO : navMenuVOs) {
            MethodInvocation mi = cachedMethodInvocations.get(navMenuVO.getId());
            if (mi != null) {
                RequiresPermissions rp = mi.getMethod().getAnnotation(RequiresPermissions.class);
                if (rp != null) {
                    boolean granted;
                    String[] permissions = rp.value();
                    if (rp.logical().equals(Logical.AND)) {
                        granted = true;
                        for (String permission : permissions) {
                            boolean grantedOne = false;
                            for (String userPrivilege : userPrivileges) {
                                if (userPrivilege.equals(permission)) {
                                    grantedOne = true;
                                    break;
                                }
                            }
                            if (grantedOne == false) {
                                granted = false;
                                break;
                            }
                        }
                    } else {
                        granted = false;
                        for (String permission : permissions) {
                            boolean grantedOne = true;
                            for (String userPrivilege : userPrivileges) {
                                if (userPrivilege.equals(permission)) {
                                    grantedOne = true;
                                    break;
                                }
                            }
                            if (grantedOne == true) {
                                granted = true;
                                break;
                            }
                        }
                    }
                    if (!granted) {
                        continue;
                    }
                }

                RequiresRoles rr = mi.getMethod().getAnnotation(RequiresRoles.class);
                if (rr != null) {
                    boolean granted;
                    String[] roles = rr.value();
                    if (rr.logical().equals(Logical.AND)) {
                        granted = true;
                        for (String role : roles) {
                            boolean grantedOne = false;
                            for (String userRole : userRoles) {
                                if (userRole.equals(role)) {
                                    grantedOne = true;
                                    break;
                                }
                            }
                            if (grantedOne == false) {
                                granted = false;
                                break;
                            }
                        }
                    } else {
                        granted = false;
                        for (String role : roles) {
                            boolean grantedOne = true;
                            for (String userRole : userRoles) {
                                if (userRole.equals(role)) {
                                    grantedOne = true;
                                    break;
                                }
                            }
                            if (grantedOne == true) {
                                granted = true;
                                break;
                            }
                        }
                    }
                    if (!granted) {
                        continue;
                    }
                }
            }
            //添加用户显示菜单项
            userNavMenuVOs.add(navMenuVO);
        }

        //移除没有子项的父项
        removeEmptyParentItem(userNavMenuVOs);

        if (logger.isDebugEnabled()) {
            logger.debug("User Menu list: {}", user.getDisplay());
            for (NavMenuVO navMenuVO : userNavMenuVOs) {
                logger.debug(" - {}", navMenuVO.getPath());
            }
        }

        return userNavMenuVOs;
    }

    public void removeEmptyParentItem(List<NavMenuVO> userNavMenuVOs) {
        List<NavMenuVO> toRemoves = Lists.newArrayList();
        for (NavMenuVO vo : userNavMenuVOs) {
            if (StringUtils.isBlank(vo.getUrl())) {
                boolean toRemove = true;
                for (NavMenuVO item : userNavMenuVOs) {
                    if (vo.getId().equals(item.getParentId())) {
                        toRemove = false;
                        break;
                    }
                }
                if (toRemove) {
                    toRemoves.add(vo);
                }
            }
        }
        if (toRemoves.size() > 0) {
            userNavMenuVOs.removeAll(toRemoves);
            removeEmptyParentItem(userNavMenuVOs);
        }
    }
}
