package lab.s2jh.aud.dao;

import lab.s2jh.aud.entity.LoggingEvent;
import lab.s2jh.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface LoggingEventDao extends BaseDao<LoggingEvent, Long> {

}