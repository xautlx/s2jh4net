package lab.s2jh.module.sys.dao;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.ConfigProperty;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyDao extends BaseDao<ConfigProperty, Long> {

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    ConfigProperty findByPropKey(String propKey);
}