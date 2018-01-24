package com.entdiy.auth.service;

import com.entdiy.auth.dao.SiteUserDao;
import com.entdiy.auth.dao.SiteUserExtDao;
import com.entdiy.auth.entity.SiteUser;
import com.entdiy.auth.entity.SiteUserExt;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SiteUserService extends BaseService<SiteUser, Long> {

    @Autowired
    private SiteUserDao siteUserDao;

    @Autowired
    private SiteUserExtDao siteUserExtDao;

    @Override
    protected BaseDao<SiteUser, Long> getEntityDao() {
        return siteUserDao;
    }

    public SiteUserExt saveExt(SiteUserExt entity) {
        return siteUserExtDao.save(entity);
    }
}
