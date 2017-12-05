package com.entdiy.sys.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.NotifyMessageReadDao;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.NotifyMessageRead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotifyMessageReadService extends BaseService<NotifyMessageRead, Long> {

    @Autowired
    private NotifyMessageReadDao notifyMessageReadDao;

    @Override
    protected BaseDao<NotifyMessageRead, Long> getEntityDao() {
        return notifyMessageReadDao;
    }

    public Integer countByNotifyMessage(NotifyMessage notifyMessage) {
        return notifyMessageReadDao.countByNotifyMessage(notifyMessage.getId());
    }

}
