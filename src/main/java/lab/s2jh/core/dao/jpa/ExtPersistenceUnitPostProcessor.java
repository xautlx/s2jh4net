package lab.s2jh.core.dao.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Maps;

/**
 * 扩展JPA Hibernate持久对象Post处理逻辑
 * 主要是为了获取MutablePersistenceUnitInfo对象从而可以获取Hibernate Entity元数据
 * 
 */
public class ExtPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ExtPersistenceUnitPostProcessor.class);

    private static Map<String, String> entityManagerMapping = Maps.newHashMap();

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        String pun = pui.getPersistenceUnitName();
        logger.debug("ManagedClassNames for PersistenceUnitName: {}", pun);
        for (String className : pui.getManagedClassNames()) {
            logger.debug(" - {}", className);
            entityManagerMapping.put(className, pun);
        }
    }

    public static EntityManager getEntityManagerByEntityClass(Class<?> clazz) {
        try {
            //TODO 进一步缓存优化
            EntityManagerFactory entityManagerFactory = (EntityManagerFactory) SpringContextHolder.getApplicationContext().getBean(
                    entityManagerMapping.get(clazz.getName()));
            EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);
            return emHolder.getEntityManager();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
