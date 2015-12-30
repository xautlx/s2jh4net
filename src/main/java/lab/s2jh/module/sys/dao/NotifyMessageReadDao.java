package lab.s2jh.module.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageReadDao extends BaseDao<NotifyMessageRead, Long> {

    NotifyMessageRead findByNotifyMessageAndReadUser(NotifyMessage notifyMessage, User user);

    @Query("from NotifyMessageRead where readUser.id=:readUserId and notifyMessage.id in (:scopeEffectiveMessageIds)")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessageRead> findByReadUserAndNotifyMessageIn(@Param("readUserId") Long readUserId,
            @Param("scopeEffectiveMessageIds") List<Long> scopeEffectiveMessageIds);

    @Query("select count(nm) from NotifyMessageRead nm where nm.notifyMessage.id=:notifyMessageId")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    Integer countByNotifyMessage(@Param("notifyMessageId") Long notifyMessageId);
}