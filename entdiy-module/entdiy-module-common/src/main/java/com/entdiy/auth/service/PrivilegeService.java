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

import com.entdiy.auth.dao.PrivilegeDao;
import com.entdiy.auth.dao.RoleDao;
import com.entdiy.auth.dao.RoleR2PrivilegeDao;
import com.entdiy.auth.entity.Privilege;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

@Service
@Transactional
public class PrivilegeService extends BaseService<Privilege, Long> {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RoleR2PrivilegeDao roleR2PrivilegeDao;

    @Autowired
    private RoleDao roleDao;

    @Value("${auth.control.level}")
    private String authControlLevel;

    @Transactional(readOnly = true)
    public List<Privilege> findAllCached() {
        return privilegeDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public Page<Privilege> findUnRelatedPrivilegesForRole(final String roleId, final GroupPropertyFilter groupFilter, Pageable pageable) {
        Specification<Privilege> specification = new Specification<Privilege>() {
            @Override
            public Predicate toPredicate(Root<Privilege> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = buildPredicatesFromFilters(groupFilter, root, query, builder);
                Subquery<RoleR2Privilege> sq = query.subquery(RoleR2Privilege.class);
                Root<RoleR2Privilege> r2 = sq.from(RoleR2Privilege.class);
                sq.where(builder.equal(r2.get("privilege"), root), builder.equal(r2.get("role").get("id"), roleId)).select(r2);
                return builder.and(predicate, builder.not(builder.exists(sq)));
            }
        };
        return privilegeDao.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public List<RoleR2Privilege> findRelatedRoleR2PrivilegesForPrivilege(String privilegeId) {
        return roleR2PrivilegeDao.findByPrivilegeId(privilegeId);
    }

    @Transactional(readOnly = true)
    public Privilege findByCode(String code) {
        return privilegeDao.findByCode(code);
    }
}
