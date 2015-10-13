package lab.s2jh.core.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.ClassUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

/**
 * 数据库数据初始化处理器触发器
 */
@Component
@Transactional
public class DatabaseDataInitializeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeExecutor.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Value("hibernate_hbm2ddl_auto:")
    private String hbm2ddl;

    @Transactional
    public void initialize(List<BaseDatabaseDataInitialize> initializeProcessors) {
        //如果不是开发模式，则直接退出防止意外更新已有数据
        //为了稳妥，生产环境数据采用手工更新方式
        if (!DynamicConfigService.isDevMode()) {
            logger.info("Skipped DatabaseDataInitializeExecutor as NOT dev mode.");
            return;
        }

        CountThread countThread = new CountThread();
        countThread.start();

        if ("create-drop".equalsIgnoreCase(hbm2ddl)) {
            logger.debug("Invoke hibernate sessionFactory.close() to trigger drop tables as hbm2ddl={}.", hbm2ddl);
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
            sessionFactory.close();
            logger.debug("Hibernate hbm2ddl drop done, force System.exit()");
            System.exit(0);
        }

        {
            //搜索所有entity对象，并自动进行自增初始化值设置
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**.entity.**"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**.entity.**"));

            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> entityClass = ClassUtils.forName(beanDefinition.getBeanClassName());
                MetaData metaData = entityClass.getAnnotation(MetaData.class);
                if (metaData != null && metaData.autoIncrementInitValue() > 0) {
                    autoIncrementInitValue(entityClass, entityManager);
                }
            }
        }

        for (BaseDatabaseDataInitialize initializeProcessor : initializeProcessors) {
            logger.debug("Invoking data initialize for {}", initializeProcessor);
            countThread.update(initializeProcessor.getClass());
            initializeProcessor.initialize(entityManager);
        }

        //停止计数线程
        countThread.shutdown();
        //清空释放所有基础和模拟数据操作缓存
        entityManager.clear();
    }

    /**
     * 初始化自增对象起始值
     */
    public static void autoIncrementInitValue(final Class<?> entity, final EntityManager entityManager) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        if (Integer.valueOf(String.valueOf(count)) > 0) {
            logger.debug("Skipped autoIncrementInitValue as exist data: {}", entity.getClass());
            return;
        }
        Session session = entityManager.unwrap(Session.class);
        session.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                Table table = entity.getAnnotation(Table.class);
                MetaData metaData = entity.getAnnotation(MetaData.class);
                Assert.isTrue(metaData.autoIncrementInitValue() > 1, "Undefined MetaData autoIncrementInitValue for entity: " + entity.getClass());

                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String name = databaseMetaData.getDatabaseProductName().toLowerCase();
                //根据不同数据库类型执行不同初始化SQL脚本
                String sql = null;
                if (name.indexOf("mysql") > -1) {
                    sql = "ALTER TABLE " + table.name() + " AUTO_INCREMENT =" + metaData.autoIncrementInitValue();
                } else if (name.indexOf("sql server") > -1) {
                    //DBCC   CHECKIDENT( 'tb ',   RESEED,   20000)  
                    sql = "DBCC CHECKIDENT('" + table.name() + "',RESEED," + metaData.autoIncrementInitValue() + ")";
                } else if (name.indexOf("h2") > -1) {
                    //DO Nothing;
                } else {
                    throw new UnsupportedOperationException(name);
                }

                if (StringUtils.isNotBlank(sql)) {
                    logger.debug("Execute autoIncrementInitValue SQL: {}", sql);
                    entityManager.createNativeQuery(sql).executeUpdate();
                }
            }
        });
    }

    private static class CountThread extends Thread {

        private Class<?> runnerClass;
        private boolean running = true;
        private Date startTime = new Date();

        @Override
        public void run() {
            while (running) {
                try {
                    if (runnerClass != null) {
                        Date now = new Date();
                        logger.info("Running at " + runnerClass.getName() + ". Total passed time "
                                + DateUtils.getHumanDisplayForTimediff(now.getTime() - startTime.getTime()) + " at Thread: "
                                + Thread.currentThread().getId());
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            super.run();
        }

        public void update(Class<?> runnerClass) {
            this.runnerClass = runnerClass;
        }

        public void shutdown() {
            logger.info("Shutdowning DatabaseDataInitializeTrigger CountThread: " + Thread.currentThread().getId());
            running = false;
        }
    }
}
