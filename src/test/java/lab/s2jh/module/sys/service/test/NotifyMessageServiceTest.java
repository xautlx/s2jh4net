package lab.s2jh.module.sys.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.service.NotifyMessageService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class NotifyMessageServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Test
    public void findMgmtCountToRead() {
        User user = new User();
        user.setId(1L);
        Integer count = notifyMessageService.findMgmtCountToRead(user);
        logger.debug("findMgmtCountToRead Count: {}", count);
    }

    @Test
    public void findMgmtPageToRead() {
        User user = new User();
        user.setId(1L);
        Page<NotifyMessage> pageData = notifyMessageService.findMgmtPageToRead(user, null, null);
        logger.debug("findMgmtPageToRead pageData: {}", pageData);
    }
}
