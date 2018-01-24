package com.entdiy.auth.dao;

import com.entdiy.auth.entity.SiteUser;
import com.entdiy.core.dao.jpa.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserDao extends BaseDao<SiteUser, Long> {

}
