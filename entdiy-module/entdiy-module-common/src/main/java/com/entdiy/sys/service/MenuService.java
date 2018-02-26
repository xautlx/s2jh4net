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
package com.entdiy.sys.service;

import com.entdiy.auth.entity.Role;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.UserR2Role;
import com.entdiy.core.service.BaseNestedSetService;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.sys.dao.MenuDao;
import com.entdiy.sys.entity.Menu;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService extends BaseNestedSetService<Menu, Long> {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuDao menuDao;

    @Transactional(readOnly = true)
    public List<Menu> findAllCached() {
        return menuDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public List<Menu> findAvailableMenus() {
        //全部排序缓存菜单数据
        List<Menu> allMenus = findAllCached();

        //停用菜单集合，某节点停用但是子节点未停用不在此集合里
        List<Menu> disabledMenus = allMenus.stream().filter(one -> one.getDisabled()).collect(Collectors.toList());

        //启用菜单集合
        List<Menu> availableMenus = allMenus.stream().filter(one -> {
            //所有没有落在停用节点区间的节点为可用节点
            return disabledMenus.stream().noneMatch(disabledMenu -> one.getLft() >= disabledMenu.getLft() && one.getRgt() <= disabledMenu.getRgt());
        }).collect(Collectors.toList());

        return availableMenus;
    }

    /**
     * 计算用户的有权限的菜单列表
     *
     * @return
     */
    public List<Menu> processUserMenu(User user) {
        //获取所有有效的菜单集合
        List<Menu> menus = findAvailableMenus();
        List<UserR2Role> userR2Roles = user.getUserR2Roles();
        Set<String> userRoles = Sets.newHashSet();
        Set<String> userPrivileges = Sets.newHashSet();
        if (userR2Roles != null) {
            for (UserR2Role userR2Role : userR2Roles) {
                Role role = userR2Role.getRole();
                //如果是超级管理员直接返回所有有效菜单
                if (role.getCode().equals(DefaultAuthUserDetails.ROLE_SUPER_USER)) {
                    return menus;
                }
                userRoles.add(role.getCode());
                List<RoleR2Privilege> roleR2Privileges = role.getRoleR2Privileges();
                for (RoleR2Privilege roleR2Privilege : roleR2Privileges) {
                    userPrivileges.add(roleR2Privilege.getPrivilege().getCode());
                }
            }
        }
        //追加管理端默认角色
        userRoles.add(DefaultAuthUserDetails.ROLE_MGMT_USER);

        List<Menu> userMenus = Lists.newArrayList();

        //计算用户有访问权限的菜单列表
        for (Menu navMenuVO : menus) {
            Menu menu = findOne(navMenuVO.getId()).get();
            if (StringUtils.isNotBlank(menu.getUrl())) {
                Method mappingMethod = menu.getMappingMethod();
                if (mappingMethod != null) {
                    RequiresPermissions rp = mappingMethod.getAnnotation(RequiresPermissions.class);
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
                                boolean grantedOne = false;
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

                    RequiresRoles rr = mappingMethod.getAnnotation(RequiresRoles.class);
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
            }
            //添加用户显示菜单项
            userMenus.add(navMenuVO);
        }

        //移除没有子项的父项
        //removeEmptyParentItem(userMenus);

        if (logger.isDebugEnabled()) {
            logger.debug("User Menu list: {}", user.getDisplay());
            for (Menu menu : userMenus) {
                logger.debug(" - {}", menu.getName());
            }
        }

        return userMenus;
    }
}
