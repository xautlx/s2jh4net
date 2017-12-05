package xyz.entdiy.shop.service;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.entdiy.shop.dao.SiteUserDao;
import xyz.entdiy.shop.entity.SiteUser;

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
