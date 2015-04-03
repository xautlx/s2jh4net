package lab.s2jh.aud.dao;

import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserLogonLogDao extends BaseDao<UserLogonLog, Long> {

    UserLogonLog findByHttpSessionId(String httpSessionId);

}