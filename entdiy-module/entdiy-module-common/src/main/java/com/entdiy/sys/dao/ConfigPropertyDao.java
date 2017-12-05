package com.entdiy.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.ConfigProperty;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyDao extends BaseDao<ConfigProperty, Long> {

    @Query("from ConfigProperty")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    List<ConfigProperty> findAllCached();

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    ConfigProperty findByPropKey(String propKey);
}