package lab.s2jh.aud.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.aud.dao.RevisionEntityDao;
import lab.s2jh.core.audit.envers.EntityRevision;
import lab.s2jh.core.audit.envers.ExtDefaultRevisionEntity;
import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RevisionEntityService extends BaseService<ExtDefaultRevisionEntity, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RevisionEntityDao revisionEntityDao;

    @Override
    protected BaseDao<ExtDefaultRevisionEntity, Long> getEntityDao() {
        return revisionEntityDao;
    }

    /**
     * 查询对象历史记录版本集合
     * 
     * @param id
     *            实体主键
     * @param property
     *            过滤属性
     * @param changed
     *            过滤方式，有无变更
     * @return
     */
    @Transactional(readOnly = true)
    public List<EntityRevision> findEntityRevisions(final Class<?> entityClass, final Object id, String property, Boolean changed) {
        List<EntityRevision> entityRevisions = Lists.newArrayList();
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(entityClass, false, true);
        auditQuery.add(AuditEntity.id().eq(id)).addOrder(AuditEntity.revisionNumber().desc());
        if (StringUtils.isNotBlank(property) && changed != null) {
            if (changed) {
                auditQuery.add(AuditEntity.property(property).hasChanged());
            } else {
                auditQuery.add(AuditEntity.property(property).hasNotChanged());
            }
        }
        List list = auditQuery.getResultList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object obj : list) {
                Object[] itemArray = (Object[]) obj;
                EntityRevision entityRevision = new EntityRevision();
                entityRevision.setEntity(itemArray[0]);
                entityRevision.setRevisionEntity((ExtDefaultRevisionEntity) itemArray[1]);
                entityRevision.setRevisionType((RevisionType) itemArray[2]);
                entityRevisions.add(entityRevision);
            }
        }
        return entityRevisions;
    }

    /**
     * 查询对象历史记录版本集合
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<EntityRevision> findEntityRevisions(final Class<?> entityClass, Number... revs) {
        List<EntityRevision> entityRevisions = Lists.newArrayList();
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(entityClass, false, true);
        auditQuery.add(AuditEntity.revisionNumber().in(revs));
        List<?> list = auditQuery.getResultList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object obj : list) {
                Object[] itemArray = (Object[]) obj;
                EntityRevision entityRevision = new EntityRevision();
                entityRevision.setEntity(itemArray[0]);
                entityRevision.setRevisionEntity((ExtDefaultRevisionEntity) itemArray[1]);
                entityRevision.setRevisionType((RevisionType) itemArray[2]);
                entityRevisions.add(entityRevision);
            }
        }
        return entityRevisions;
    }
}
