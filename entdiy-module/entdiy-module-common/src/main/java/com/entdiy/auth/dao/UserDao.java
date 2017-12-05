package com.entdiy.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, Long> {

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByAuthTypeAndAuthUid(AuthTypeEnum authType, String authUid);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByAuthTypeAndAccessToken(AuthTypeEnum authType, String accessToken);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByAuthUid(String authUid);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<User> findByEmail(String email);

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<User> findByMobile(String mobile);
}