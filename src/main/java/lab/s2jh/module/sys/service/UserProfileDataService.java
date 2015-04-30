package lab.s2jh.module.sys.service;

import java.util.List;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.dao.UserProfileDataDao;
import lab.s2jh.module.sys.entity.UserProfileData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileDataService extends BaseService<UserProfileData, Long> {

    @Autowired
    private UserProfileDataDao userProfileDataDao;

    @Override
    protected BaseDao<UserProfileData, Long> getEntityDao() {
        return userProfileDataDao;
    }

    @Transactional(readOnly = true)
    public UserProfileData findByUserAndCode(User user, String code) {
        return userProfileDataDao.findByUserAndCode(user, code);
    }

    @Transactional(readOnly = true)
    public List<UserProfileData> findByUser(User user) {
        return userProfileDataDao.findByUser(user);
    }
}
