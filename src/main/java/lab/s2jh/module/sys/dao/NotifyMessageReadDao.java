package lab.s2jh.module.sys.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;

import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageReadDao extends BaseDao<NotifyMessageRead, Long> {
    NotifyMessageRead findByNotifyMessageAndReadUser(NotifyMessage notifyMessage, User user);
}