package lab.s2jh.core.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import lab.s2jh.support.service.DynamicConfigService;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库基础数据初始化处理器
 */
public abstract class BaseDatabaseDataInitialize {

    private final static Logger logger = LoggerFactory.getLogger(BaseDatabaseDataInitialize.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Transactional
    public void initialize() {
        String hbm2ddl = dynamicConfigService.getString("hibernate_hbm2ddl_auto");
        if ("create-drop".equalsIgnoreCase(hbm2ddl)) {
            logger.debug("Invoke hibernate sessionFactory.close() to trigger drop tables as hbm2ddl={}.", hbm2ddl);
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
            sessionFactory.close();
            logger.debug("Hibernate hbm2ddl drop done, force System.exit()");
            System.exit(0);
        }
        initializeInternal();
    }

    public abstract void initializeInternal();
}
