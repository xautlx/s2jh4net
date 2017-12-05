package com.entdiy.aud.service;

import com.entdiy.aud.dao.LoggingEventDao;
import com.entdiy.aud.entity.LoggingEvent;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoggingEventService extends BaseService<LoggingEvent, Long> {

    @Autowired
    private LoggingEventDao loggingEventDao;

    @Override
    protected BaseDao<LoggingEvent, Long> getEntityDao() {
        return loggingEventDao;
    }
}
