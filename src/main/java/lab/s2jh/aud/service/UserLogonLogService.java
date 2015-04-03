package lab.s2jh.aud.service;

import lab.s2jh.aud.dao.UserLogonLogDao;
import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLogonLogService extends BaseService<UserLogonLog, Long> {

    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Override
    protected BaseDao<UserLogonLog, Long> getEntityDao() {
        return userLogonLogDao;
    }

    @Transactional(readOnly = true)
    public UserLogonLog findBySessionId(String httpSessionId) {
        return userLogonLogDao.findByHttpSessionId(httpSessionId);
    }
}
