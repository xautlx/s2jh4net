package com.entdiy.auth.dao;

import com.entdiy.auth.entity.UserExt;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserExtDao extends BaseDao<UserExt, Long> {

    UserExt findByRandomCode(String randomCode);
}