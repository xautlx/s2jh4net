package lab.s2jh.support.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.UserExt;
import lab.s2jh.module.auth.service.UserService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryCacheTest extends SpringTransactionalTestCase {

    @Autowired
    private UserService userService;

    @Test
    public void query() {

        User entity1 = MockEntityUtils.buildMockObject(User.class);
        entity1.setAuthUid("user1");
        entity1.setEmail("user1@abc.com");
        userService.save(entity1, "123");

        User entity2 = MockEntityUtils.buildMockObject(User.class);
        entity2.setAuthUid("user2");
        entity2.setEmail("user2@abc.com");
        userService.save(entity2, "123");

        logger.debug("05. Query user1...");
        UserExt userExt = entity1.getUserExt();
        userExt.setLogonTimes(userExt.getLogonTimes() + 1);
        userService.saveExt(userExt);

        logger.debug("08. Query user1...");
        userService.findOne(entity1.getId());

        logger.debug("10. Query user1...");
        userService.findByAuthUid("user1");

        logger.debug("20. Query user1...");
        userService.findByAuthUid("user1");

        logger.debug("30. Update user2 to user3...");
        entity2.setAuthUid("user3");
        userService.save(entity2);

        logger.debug("40. Query user1...");
        userService.findByAuthUid("user1");

        logger.debug("50. Update user1 to user4...");
        entity1.setAuthUid("user4");
        userService.save(entity1);

        logger.debug("60. Query user1...");
        userService.findByAuthUid("user1");
    }
}
