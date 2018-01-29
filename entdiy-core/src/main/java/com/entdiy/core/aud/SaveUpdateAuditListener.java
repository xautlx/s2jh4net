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
package com.entdiy.core.aud;

import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.core.util.DateUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 审计记录记录创建和修改信息
 *
 * @see AuditingEntityListener
 */
public class SaveUpdateAuditListener {

    @PrePersist
    public void touchForCreate(Object target) {
        if (!(target instanceof DefaultAuditable)) {
            return;
        }
        DefaultAuditable auditable = (DefaultAuditable) target;
        if (auditable.getCreateDate() == null) {
            auditable.setCreateDate(DateUtils.currentDateTime());
        }

        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails != null) {
            auditable.setCreateUserName(authUserDetails.getUsername());
            auditable.setDataDomain(authUserDetails.getDataDomain());
            auditable.setCreateAccountId(authUserDetails.getAccountId());
        }
    }


    @PreUpdate
    public void touchForUpdate(Object target) {
        if (!(target instanceof DefaultAuditable)) {
            return;
        }
        DefaultAuditable auditable = (DefaultAuditable) target;
        auditable.setUpdateDate(DateUtils.currentDateTime());

        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails != null) {
            auditable.setUpdateUserName(authUserDetails.getUsername());
            auditable.setUpdateAccountId(authUserDetails.getAccountId());
        }
    }
}
