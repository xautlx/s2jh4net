package com.entdiy.aud.service;

import com.entdiy.aud.dao.SendMessageLogDao;
import com.entdiy.aud.entity.SendMessageLog;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SendMessageLogService extends BaseService<SendMessageLog, Long> {

    @Autowired
    private SendMessageLogDao sendMessageLogDao;

    @Override
    protected BaseDao<SendMessageLog, Long> getEntityDao() {
        return sendMessageLogDao;
    }

    @Async
    public void asyncSave(SendMessageLog entity) {
        sendMessageLogDao.save(entity);
    }
}
