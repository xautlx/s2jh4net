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

    @Query("from NotifyMessage where effective=true order by orderRank desc,publishTime desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessage> findEffectiveMessages();

    @Query("from NotifyMessage where effective=true and authRequired=false order by orderRank desc,publishTime desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessage> findEffectivePubMessages();

    @Query("from NotifyMessage where (effective is null or (effective=true and expireTime is not null))")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<NotifyMessage> findTobeEffectiveMessages();
}