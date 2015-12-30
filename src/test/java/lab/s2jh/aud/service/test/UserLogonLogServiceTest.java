package lab.s2jh.aud.service.test;

import java.util.List;
import java.util.Map;

import lab.s2jh.aud.service.UserLogonLogService;
import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserLogonLogServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private UserLogonLogService userLogonLogService;

    @Test
    public void findGroupByLogonDay() {
        List<Map<String, Object>> items = userLogonLogService.findGroupByLogonDay();
        for (Map<String, Object> item : items) {
            logger.debug("Item: {}", item);
        }
    }
}
