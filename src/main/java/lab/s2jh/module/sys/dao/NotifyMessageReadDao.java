package lab.s2jh.module.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageReadDao extends BaseDao<NotifyMessageRead, Long> {

    NotifyMessageRead findByNotifyMessageAndReadUser(NotifyMessage notifyMessage, User user);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessageRead> findByReadUserAndNotifyMessageIn(User readUser, List<NotifyMessage> scopeEffectiveMessages);
}