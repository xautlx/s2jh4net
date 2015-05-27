package lab.s2jh.aud.dao;


import lab.s2jh.aud.entity.SendMessageLog;
import lab.s2jh.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface SendMessageLogDao extends BaseDao<SendMessageLog, Long> {

}