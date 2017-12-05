package com.entdiy.aud.dao;


import com.entdiy.aud.entity.SendMessageLog;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface SendMessageLogDao extends BaseDao<SendMessageLog, Long> {

}