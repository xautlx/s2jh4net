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

import com.entdiy.auth.dao.OauthAccountDao;
import com.entdiy.auth.entity.Account;
import com.entdiy.auth.entity.OauthAccount;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OauthAccountService extends BaseService<OauthAccount, Long> {

    @Autowired
    private OauthAccountDao oauthAccountDao;

    @Transactional(readOnly = true)
    public OauthAccount findByOauthOpenIdAndOauthTypeAndAuthType(String oauthOpenId, GlobalConstant.OauthTypeEnum oauthType, Account.AuthTypeEnum authType) {
        return oauthAccountDao.findByOauthOpenIdAndOauthTypeAndAuthType(oauthOpenId, oauthType, authType);
    }

    @Transactional(readOnly = true)
    public OauthAccount findByOauthOpenIdAndOauthType(String oauthOpenId, GlobalConstant.OauthTypeEnum oauthType) {
        return findByOauthOpenIdAndOauthTypeAndAuthType(oauthOpenId, oauthType, Account.AuthTypeEnum.site);
    }

    @Transactional(readOnly = true)
    public List<OauthAccount> findByAccountAndOauthTypeAndAuthType(Account account, GlobalConstant.OauthTypeEnum oauthType, Account.AuthTypeEnum authType) {
        return oauthAccountDao.findByAccountAndOauthTypeAndAuthType(account, oauthType, authType);
    }

    @Transactional(readOnly = true)
    public OauthAccount findByAccountAndOauthOpenIdAndOauthTypeAndAuthType(Account account, String oauthOpenId, GlobalConstant.OauthTypeEnum oauthType, Account.AuthTypeEnum authType) {
        return oauthAccountDao.findByAccountAndOauthOpenIdAndOauthTypeAndAuthType(account, oauthOpenId, oauthType, authType);
    }
}
