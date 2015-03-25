package lab.s2jh.module.auth.dao;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, Long> {

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByAuthTypeAndAuthUid(AuthTypeEnum authType, String authUid);
}