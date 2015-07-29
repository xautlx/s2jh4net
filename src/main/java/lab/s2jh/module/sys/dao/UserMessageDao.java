package lab.s2jh.module.sys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.UserMessage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageDao extends BaseDao<UserMessage, Long> {

    @Modifying
    @Query("update UserMessage set effective = true where publishTime<=:now and effective = false")
    Integer updateUserMessageEffective(@Param("now") Date now);

    @Modifying
    @Query("update UserMessage set effective = false where expireTime<=:now " + "and effective = true and readTotalCount = 0")
    Integer updateUserMessageNoneffective(@Param("now") Date now);

    @Query("from UserMessage where effective=true and appPush=true and lastPushTime is null order by publishTime desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<UserMessage> findEffectiveMessages();
}