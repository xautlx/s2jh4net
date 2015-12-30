package lab.s2jh.module.sys.service;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.sys.dao.NotifyMessageReadDao;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;

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
