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
package com.entdiy.sys.dao;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.SmsVerifyCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;

@Repository
public interface SmsVerifyCodeDao extends BaseDao<SmsVerifyCode, Long> {

    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    SmsVerifyCode findByMobileNum(@Param("mobileNum") String mobileNum);

    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    @Query("select count(1) from SmsVerifyCode where firstVerifiedTime is null")
    int countTodoItems();

    @Modifying
    @Query("delete SmsVerifyCode where firstVerifiedTime is null and expireTime<:now")
    int batchDeleteExpireItems(@Param("now") LocalDateTime now);
}