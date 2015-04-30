package lab.s2jh.module.sys.service.test;

import java.util.Date;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.service.NotifyMessageService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyMessageServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private NotifyMessageService notifyMessageService;

//    @Test
    public void findSiteCountToRead() {
        User user = new User();
        user.setId(1L);
        Long count = notifyMessageService.findCountToRead(user, "web-admin");
        logger.debug("findSiteCountToRead Count: {}", count);
    }
    
    @Test
    public void update(){
    	notifyMessageService.updateNotifyMessageEffective(new Date());
    	notifyMessageService.updateNotifyMessageNoneffective(new Date());
    }

}
