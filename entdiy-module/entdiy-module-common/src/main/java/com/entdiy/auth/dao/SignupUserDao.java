package com.entdiy.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import com.entdiy.auth.entity.SignupUser;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupUserDao extends BaseDao<SignupUser, Long> {
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    SignupUser findByAuthUid(String authUid);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<SignupUser> findByEmail(String email);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<SignupUser> findByMobile(String mobile);
}