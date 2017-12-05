package com.entdiy.aud.dao;

import com.entdiy.aud.entity.UserLogonLog;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserLogonLogDao extends BaseDao<UserLogonLog, Long> {

    UserLogonLog findByHttpSessionId(String httpSessionId);

}