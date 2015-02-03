package lab.s2jh.module.auth.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.UserLogonLog;

import org.springframework.stereotype.Repository;

@Repository
public interface UserLogonLogDao extends BaseDao<UserLogonLog, Long> {

    UserLogonLog findByHttpSessionId(String httpSessionId);

}