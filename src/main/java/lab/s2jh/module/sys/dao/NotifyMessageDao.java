package lab.s2jh.module.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.NotifyMessage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageDao extends BaseDao<NotifyMessage, Long> {

    @Query("from NotifyMessage where effective=true order by publishTime desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessage> findEffectiveMessages();

    @Query("from NotifyMessage where effective=true and authRequired=false order by publishTime desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessage> findEffectivePubMessages();
}