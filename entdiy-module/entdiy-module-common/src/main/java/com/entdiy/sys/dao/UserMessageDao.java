package com.entdiy.sys.dao;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.UserMessage;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageDao extends BaseDao<UserMessage, Long> {

}