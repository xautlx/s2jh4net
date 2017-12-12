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
package com.entdiy.auth.service;

import com.entdiy.auth.dao.RoleDao;
import com.entdiy.auth.dao.SignupUserDao;
import com.entdiy.auth.dao.UserDao;
import com.entdiy.auth.dao.UserExtDao;
import com.entdiy.auth.entity.SignupUser;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.UserExt;
import com.entdiy.auth.entity.UserR2Role;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.UidUtils;
import com.entdiy.security.PasswordService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SignupUserService extends BaseService<SignupUser, Long> {

    @Autowired
    private SignupUserDao signupUserDao;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserExtDao userExtDao;

    @Override
    protected BaseDao<SignupUser, Long> getEntityDao() {
        return signupUserDao;
    }

    @Transactional(readOnly = true)
    public List<SignupUser> findByEmail(String email) {
        return signupUserDao.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<SignupUser> findByMobile(String mobile) {
        return signupUserDao.findByMobile(mobile);
    }

    public SignupUser findByAuthUid(String authUid) {
        return signupUserDao.findByAuthUid(authUid);
    }

    public String encodeUserPasswd(SignupUser signupUser, String rawPassword) {
        return passwordService.entryptPassword(rawPassword, signupUser.getAuthGuid());
    }

    public SignupUser signup(SignupUser entity, String rawPassword) {
        entity.setSignupTime(DateUtils.currentDate());
        entity.setAuthGuid(UidUtils.buildUID());
        entity.setPassword(encodeUserPasswd(entity, rawPassword));
        return signupUserDao.save(entity);
    }

    public User auditNewUser(SignupUser entity) {
        User user = entity.getUser();
        Long[] selectedRoleIds = user.getSelectedRoleIds();
        if (selectedRoleIds != null && selectedRoleIds.length > 0) {
            List<UserR2Role> userR2Roles = Lists.newArrayList();
            for (Long selectedRoleId : selectedRoleIds) {
                UserR2Role r2 = new UserR2Role();
                r2.setUser(user);
                r2.setRole(roleDao.findOne(selectedRoleId));
                userR2Roles.add(r2);
            }
            user.setUserR2Roles(userR2Roles);
        }
        user.setAuthUid(entity.getAuthUid());
        user.setAuthGuid(entity.getAuthGuid());
        user.setPassword(entity.getPassword());
        user.setMobile(entity.getMobile());
        user.setEmail(entity.getEmail());
        user.setTrueName(entity.getTrueName());
        user.setNickName(entity.getNickName());
        if (StringUtils.isBlank(user.getNickName())) {
            user.setNickName(user.getAuthUid());
        }

        userDao.save(user);

        UserExt userExt = new UserExt();
        userExt.setId(user.getId());
        userExt.setSignupTime(entity.getSignupTime());
        userExtDao.save(userExt);

        entity.setAuditTime(DateUtils.currentDate());
        signupUserDao.save(entity);

        return user;
    }
}
