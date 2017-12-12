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
package com.entdiy.aud.service;

import java.util.List;
import java.util.Map;

import com.entdiy.aud.dao.UserLogonLogDao;
import com.entdiy.aud.entity.UserLogonLog;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.dao.mybatis.MyBatisDao;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLogonLogService extends BaseService<UserLogonLog, Long> {

    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    protected BaseDao<UserLogonLog, Long> getEntityDao() {
        return userLogonLogDao;
    }

    @Transactional(readOnly = true)
    public UserLogonLog findBySessionId(String httpSessionId) {
        return userLogonLogDao.findByHttpSessionId(httpSessionId);
    }

    public List<Map<String, Object>> findGroupByLogonDay() {
        return myBatisDao.findList(UserLogonLog.class.getName(), "findGroupByLogonDay", null);
    }
}
