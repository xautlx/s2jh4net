package com.entdiy.auth.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.entdiy.auth.dao.PrivilegeDao;
import com.entdiy.auth.dao.RoleDao;
import com.entdiy.auth.dao.RoleR2PrivilegeDao;
import com.entdiy.auth.entity.Privilege;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeService extends BaseService<Privilege, Long> {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RoleR2PrivilegeDao roleR2PrivilegeDao;

    @Autowired
    private RoleDao roleDao;

    @Value("${auth_control_level}")
    private String authControlLevel;

    @Override
    protected BaseDao<Privilege, Long> getEntityDao() {
        return privilegeDao;
    }

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
        return roleR2PrivilegeDao.findByPrivilege_Id(privilegeId);
    }

    @Override
    public Privilege save(Privilege entity) {
        return super.save(entity);
    }

    @Override
    public void delete(Privilege entity) {
        super.delete(entity);
    }

    @Override
    public List<Privilege> save(Iterable<Privilege> entities) {
        return super.save(entities);
    }

    @Override
    public void delete(Iterable<Privilege> entities) {
        super.delete(entities);
    }
}
