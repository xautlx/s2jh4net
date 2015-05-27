package lab.s2jh.module.auth.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private UserService userService;

    @Test
    public void findDetachedOne() {
        User user = new User();
        user.setId(1L);
        userService.findDetachedOne(user.getId(), "userR2Roles");
    }

    @Test
    public void findByAuthTypeAndAuthUid() {
        User entity = MockEntityUtils.buildMockObject(User.class);
        entity.setEmail("123@abc.com");
        userService.save(entity);
        logger.debug("1...");
        userService.findByAuthTypeAndAuthUid(entity.getAuthType(), entity.getAuthUid());
        logger.debug("2...");
        userService.findByAuthTypeAndAuthUid(entity.getAuthType(), entity.getAuthUid());
        
        logger.debug("3...");
        userService.findByAuthTypeAndAccessToken(entity.getAuthType(), entity.getAccessToken());
        logger.debug("4...");
        userService.findByAuthTypeAndAccessToken(entity.getAuthType(), entity.getAccessToken());
    }
}
