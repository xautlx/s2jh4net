package lab.s2jh.module.auth.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.UserExt;

import org.springframework.stereotype.Repository;

@Repository
public interface UserExtDao extends BaseDao<UserExt, Long> {

    UserExt findByRandomCode(String randomCode);
}