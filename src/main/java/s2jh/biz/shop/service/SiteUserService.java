package s2jh.biz.shop.service;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import s2jh.biz.shop.dao.SiteUserDao;
import s2jh.biz.shop.entity.SiteUser;

@Service
@Transactional
public class SiteUserService extends BaseService<SiteUser, Long> {

    @Autowired
    private SiteUserDao siteUserDao;

    @Autowired
    private UserService userService;

    @Override
    protected BaseDao<SiteUser, Long> getEntityDao() {
        return siteUserDao;
    }

    public SiteUser findByUser(User user) {
        return siteUserDao.findByUser(user);
    }
}
