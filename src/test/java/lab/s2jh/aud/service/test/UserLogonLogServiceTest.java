package lab.s2jh.aud.service.test;

import java.util.List;
import java.util.Map;

import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.aud.service.UserLogonLogService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.util.MockEntityUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserLogonLogServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private UserLogonLogService userLogonLogService;

    @Test
    public void findGroupByLogonDay() {

        for (int i = 0; i < 10; i++) {
            UserLogonLog entity = MockEntityUtils.buildMockObject(UserLogonLog.class);
            userLogonLogService.save(entity);
        }

        List<Map<String, Object>> items = userLogonLogService.findGroupByLogonDay();
        for (Map<String, Object> item : items) {
            logger.debug("Item: {}", item);
        }
    }
}
