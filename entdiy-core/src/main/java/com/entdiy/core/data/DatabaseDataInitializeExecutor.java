/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.data;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.core.util.DateUtils;
import com.google.common.collect.Sets;
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

import javax.persistence.*;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Value("${base_packages}")
    private String basePackages;

    @Value("${hibernate_hbm2ddl_auto:}")
    private String hbm2ddl;

    @Autowired
    private List<AbstractDatabaseDataInitializeProcessor> initializeProcessors;

    @Transactional
    public void initialize() {
        CountThread countThread = new CountThread();
        countThread.start();

        //必须是开发模式才允许删除数据库
        if (GlobalConfigService.isDevMode() && "create-drop".equalsIgnoreCase(hbm2ddl)) {
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
            scan.addIncludeFilter(new AnnotationTypeFilter(MetaData.class));
            for (String pkg : basePackages.split(",")) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }

            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> entityClass = ClassUtils.forName(beanDefinition.getBeanClassName());
                MetaData metaData = entityClass.getAnnotation(MetaData.class);
                if (metaData != null && metaData.autoIncrementInitValue() > 0) {
                    autoIncrementInitValue(entityClass, entityManager);
                }
            }
        }

        for (AbstractDatabaseDataInitializeProcessor initializeProcessor : initializeProcessors) {
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
            @Override
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
                } else if (name.indexOf("oracle") > -1) {
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
