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
package com.entdiy.dev.demo.service;

import com.entdiy.auth.dao.AccountDao;
import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entdiy.dev.demo.dao.DemoSiteUserDao;
import com.entdiy.dev.demo.entity.DemoSiteUser;

@Service
@Transactional
public class DemoSiteUserService extends BaseService<DemoSiteUser, Long> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DemoSiteUserDao siteUserDao;

    @Autowired
    private AccountDao accountDao;

    @Transactional(readOnly = true)
    public DemoSiteUser findByAccount(Account account) {
        return siteUserDao.findByAccount(accountDao.getOne(account.getId()));
    }

    public DemoSiteUser saveCascadeAccount(DemoSiteUser entity, String rawPassword) {
        if (StringUtils.isNotBlank(rawPassword)) {
            accountService.save(entity.getAccount(), rawPassword);
        }
        return save(entity);
    }
}
