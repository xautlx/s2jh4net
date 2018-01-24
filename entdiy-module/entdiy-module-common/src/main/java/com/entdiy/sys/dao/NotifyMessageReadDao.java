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
package com.entdiy.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import com.entdiy.auth.entity.User;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.NotifyMessageRead;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageReadDao extends BaseDao<NotifyMessageRead, Long> {

    NotifyMessageRead findByNotifyMessageAndReadUser(NotifyMessage notifyMessage, User user);

    @Query("from NotifyMessageRead where readUser.id=:readUserId and notifyMessage.id in (:scopeEffectiveMessageIds)")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessageRead> findByReadUserAndNotifyMessageIn(@Param("readUserId") Long readUserId,
            @Param("scopeEffectiveMessageIds") List<Long> scopeEffectiveMessageIds);

    @Query("select count(nm) from NotifyMessageRead nm where nm.notifyMessage.id=:notifyMessageId")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    Integer countByNotifyMessage(@Param("notifyMessageId") Long notifyMessageId);
}