package lab.s2jh.module.auth.service;

import java.util.List;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.dao.PrivilegeDao;
import lab.s2jh.module.auth.dao.RoleDao;
import lab.s2jh.module.auth.dao.RoleR2PrivilegeDao;
import lab.s2jh.module.auth.dao.UserR2RoleDao;
import lab.s2jh.module.auth.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService extends BaseService<Role, Long> {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private UserR2RoleDao userR2RoleDao;

    @Autowired
    private RoleR2PrivilegeDao roleR2PrivilegeDao;

    @Override
    protected BaseDao<Role, Long> getEntityDao() {
        return roleDao;
    }

    @Transactional(readOnly = true)
    public List<Role> findAllCached() {
        return roleDao.findAllCached();
    }

    public void updateRelatedPrivilegeR2s(Role entity, Long[] privielgeIds) {
        super.updateRelatedR2s(entity, privielgeIds, "roleR2Privileges", "privilege");
    }
}
