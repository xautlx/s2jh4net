package com.entdiy.aud.dao;

import com.entdiy.aud.entity.LoggingEvent;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface LoggingEventDao extends BaseDao<LoggingEvent, Long> {

}