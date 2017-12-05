package xyz.entdiy.shop.dao;

import com.entdiy.auth.entity.User;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import xyz.entdiy.shop.entity.SiteUser;

@Repository
public interface SiteUserDao extends BaseDao<SiteUser, Long> {

    SiteUser findByUser(User user);
}
