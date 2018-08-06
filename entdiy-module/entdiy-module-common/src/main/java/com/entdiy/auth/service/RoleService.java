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
package com.entdiy.auth.service;

import com.entdiy.auth.dao.RoleDao;
import com.entdiy.auth.entity.Role;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.UserR2Role;
import com.entdiy.core.service.BaseService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService extends BaseService<Role, Long> {

    @Autowired
    private RoleDao roleDao;

    @Transactional(readOnly = true)
    public List<Role> findAllCached() {
        return roleDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public Role findByCode(String code) {
        return roleDao.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<User> findUsersByRole(String roleCode) {
        Role role = roleDao.findByCode(roleCode);
        List<User> users = Lists.newArrayList();
        List<UserR2Role> roleR2Users = role.getRoleR2Users();
        for (UserR2Role userR2Role : roleR2Users) {
            users.add(userR2Role.getUser());
        }
        return users;
    }
}
