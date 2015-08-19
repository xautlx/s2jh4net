package lab.s2jh.core.hib.search;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

/**
 * ORM数据保存后自动更新Hibernate Search索引
 * @see org.hibernate.search.indexes.interceptor.DontInterceptEntityInterceptor
 */
@SuppressWarnings("rawtypes")
public class AutoEntityIndexingInterceptor implements EntityIndexingInterceptor<Persistable> {

    private final static Logger logger = LoggerFactory.getLogger(AutoEntityIndexingInterceptor.class);

    @Override
    public IndexingOverride onAdd(Persistable entity) {
        logger.debug("AutoEntityIndexingInterceptor onAdd: {}", entity);
        return IndexingOverride.UPDATE;
    }

    @Override
    public IndexingOverride onUpdate(Persistable entity) {
        logger.debug("AutoEntityIndexingInterceptor onUpdate: {}", entity);
        return IndexingOverride.UPDATE;
    }

    @Override
    public IndexingOverride onDelete(Persistable entity) {
        logger.debug("AutoEntityIndexingInterceptor onDelete: {}", entity);
        return IndexingOverride.REMOVE;
    }

    @Override
    public IndexingOverride onCollectionUpdate(Persistable entity) {
        logger.debug("AutoEntityIndexingInterceptor onCollectionUpdate: {}", entity);
        return IndexingOverride.UPDATE;
    }
}