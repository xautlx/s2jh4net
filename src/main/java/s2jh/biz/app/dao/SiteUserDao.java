package s2jh.biz.app.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;

import org.springframework.stereotype.Repository;

import s2jh.biz.app.entity.SiteUser;

@Repository
public interface SiteUserDao extends BaseDao<SiteUser, Long> {

    SiteUser findByUser(User user);
}
