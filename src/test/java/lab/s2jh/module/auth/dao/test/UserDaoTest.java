package lab.s2jh.module.auth.dao.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.dao.UserDao;
import lab.s2jh.module.auth.entity.User;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private UserDao userDao;

    @Test
    public void findByAuthTypeAndAuthUid() {
        User entity = MockEntityUtils.buildMockObject(User.class);
        entity.setEmail("123@abc.com");
        userDao.save(entity);

        logger.debug("1 Find before cache...");
        userDao.findOne(entity.getId()).getAccessToken();
        logger.debug("2 Find after cache...");
        userDao.findOne(entity.getId()).getAccessToken();

        logger.debug("3 Query before cache...");
        userDao.findByAuthTypeAndAuthUid(entity.getAuthType(), entity.getAuthUid()).getAccessToken();
        logger.debug("4 Query after cache...");
        userDao.findByAuthTypeAndAuthUid(entity.getAuthType(), entity.getAuthUid()).getAccessToken();
    }
}
