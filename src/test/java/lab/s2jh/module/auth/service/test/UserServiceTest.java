package lab.s2jh.module.auth.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
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
}
