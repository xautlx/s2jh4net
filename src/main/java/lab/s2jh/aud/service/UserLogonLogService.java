package lab.s2jh.aud.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.aud.dao.UserLogonLogDao;
import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.dao.mybatis.MyBatisDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLogonLogService extends BaseService<UserLogonLog, Long> {

    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    protected BaseDao<UserLogonLog, Long> getEntityDao() {
        return userLogonLogDao;
    }

    @Transactional(readOnly = true)
    public UserLogonLog findBySessionId(String httpSessionId) {
        return userLogonLogDao.findByHttpSessionId(httpSessionId);
    }

    public List<Map<String, Object>> findGroupByLogonDay() {
        return myBatisDao.findList(UserLogonLog.class.getName(), "findGroupByLogonDay", null);
    }
}
