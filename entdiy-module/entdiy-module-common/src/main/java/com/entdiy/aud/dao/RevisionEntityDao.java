package com.entdiy.aud.dao;

import com.entdiy.aud.envers.ExtDefaultRevisionEntity;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface RevisionEntityDao extends BaseDao<ExtDefaultRevisionEntity, Long> {

}
