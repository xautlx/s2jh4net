package lab.s2jh.aud.dao;

import org.springframework.stereotype.Repository;

import lab.s2jh.core.audit.envers.ExtDefaultRevisionEntity;
import lab.s2jh.core.dao.jpa.BaseDao;

@Repository
public interface RevisionEntityDao extends BaseDao<ExtDefaultRevisionEntity, Long> {

}
