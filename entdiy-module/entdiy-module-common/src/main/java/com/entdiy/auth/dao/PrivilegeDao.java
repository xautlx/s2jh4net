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
package com.entdiy.auth.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.QueryHint;

import com.entdiy.auth.entity.Privilege;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDao extends BaseDao<Privilege, Long> {

    @Query("from Privilege order by code asc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Privilege> findAllCached();

    Iterable<Privilege> findByDisabled(Boolean disabled);

    Privilege findByCode(String code);

    /**
     * 基于角色代码集合查询关联的启用状态的权限集合
     * @param roleCodes
     * @return
     */
    @Query("select distinct p from Privilege p,RoleR2Privilege r2p,Role r " + "where p=r2p.privilege and r2p.role=r "
            + "and r.code in (:roleCodes) and r.disabled=false and p.disabled=false order by p.code asc")
    List<Privilege> findPrivileges(@Param("roleCodes") Set<String> roleCodes);
}
