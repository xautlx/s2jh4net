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
package com.entdiy.support.data;

import com.entdiy.auth.entity.Privilege;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.sys.entity.Menu;
import com.entdiy.sys.service.MenuService;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于Controller的@MenuData注解重建基础数据。在 spring-mvc.xml 中声明使用。
 */
public class ControllerMetaDataPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMetaDataPostProcessor.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    public void initialize() {
        List<Menu> menus = menuService.findAllCached();
        List<Privilege> privileges = privilegeService.findAllCached();
        //只有在开发模式或者菜单数据为空，才执行菜单数据重建处理，以提高启动运效率
        if (AppContextHolder.isDevMode() || CollectionUtils.isEmpty(menus) || CollectionUtils.isEmpty(privileges)) {
            logger.debug("Rebuilding menu and privilege data ...");
            //合并所有类中所有RequiresPermissions定义信息
            Set<String> mergedPermissions = Sets.newLinkedHashSet();
            //合并所有类中所有RequiresPermissions定义信息
            Set<String> mergedMenus = Sets.newHashSet();

            requestMappingHandlerMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
                Method method = handlerMethod.getMethod();
                //菜单数据处理
                MenuData menuData = method.getAnnotation(MenuData.class);
                if (menuData != null) {
                    String[] fullPaths = menuData.value();
                    for (String fullPath : fullPaths) {
                        String[] names = fullPath.split(":");
                        for (int i = 0; i < names.length; i++) {
                            String path = StringUtils.join(names, ":", 0, i + 1);
                            if (!mergedMenus.contains(path)) {
                                //记录已处理path，下次循环跳过
                                mergedMenus.add(path);
                                //取数据库记录，如果没有则创建
                                Menu item = menus.stream().filter(one -> one.getPath().equals(path)).findFirst().orElse(null);
                                if (item == null) {
                                    item = new Menu();
                                    menus.add(item);
                                }
                                item.setPath(path);
                                String parentPath = StringUtils.join(names, ":", 0, i);
                                item.setParent(menus.stream().filter(one -> one.getPath().equals(parentPath)).findFirst().orElse(menuService.findRoot()));
                                item.setName(names[i]);
                                //计算菜单对应URL路径
                                if (i + 1 == names.length) {
                                    item.setUrl(info.getPatternsCondition().getPatterns().stream().findFirst().get());
                                    item.setControllerClass(handlerMethod.getBeanType().getName());
                                    item.setControllerMethod(method.getName());
                                }
                                item.setDisabled(menuData.disabled());
                                menuService.save(item);
                            }

                        }
                    }
                }

                //菜单数据处理
                RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class);
                if (rp != null) {
                    String[] perms = rp.value();
                    for (String perm : perms) {
                        mergedPermissions.add(StringUtils.substringBeforeLast(perm, ":") + ":*");
                        mergedPermissions.add(perm);
                    }
                }
            });

            //移除代码定义已不存在的菜单项
            //menuService.deleteAll(menus.stream().filter(one -> !one.isRoot() && !mergedMenus.contains(one.getPath())).collect(Collectors.toSet()));

            //追加新增权限定义选项
            mergedPermissions.forEach(code -> {
                if (privileges.stream().noneMatch(one -> one.getCode().equals(code))) {
                    Privilege entity = new Privilege();
                    entity.setCode(code);
                    privilegeService.save(entity);
                }
            });
            //移除代码定义已不存在的权限项
            privilegeService.deleteAll(privileges.stream().filter(one -> !mergedPermissions.contains(one.getCode())).collect(Collectors.toSet()));
        }
    }
}
