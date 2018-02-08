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
package com.entdiy.aud.service;

import com.entdiy.aud.dao.AccountLogonLogDao;
import com.entdiy.aud.entity.AccountLogonLog;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.dao.mybatis.MyBatisDao;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AccountLogonLogService extends BaseService<AccountLogonLog, Long> {

    @Autowired
    private AccountLogonLogDao accountLogonLogDao;

    @Autowired
    private MyBatisDao myBatisDao;

    @Transactional(readOnly = true)
    public AccountLogonLog findBySessionId(String httpSessionId) {
        return accountLogonLogDao.findByHttpSessionId(httpSessionId);
    }

    public List<Map<String, Object>> findGroupByLogonDay() {
        return myBatisDao.findList(AccountLogonLog.class.getName(), "findGroupByLogonDay", null);
    }
}
