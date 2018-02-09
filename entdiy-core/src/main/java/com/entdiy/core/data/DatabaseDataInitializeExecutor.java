/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.data;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 数据库数据初始化处理器触发器
 */
@Component
public class DatabaseDataInitializeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeExecutor.class);

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Value("${auto.data.skip:false}")
    private boolean autoDataSkip;

    @Autowired
    private List<AbstractDatabaseDataInitializeProcessor> initializeProcessors;

    @PostConstruct
    public void initialize() {
        if (autoDataSkip) {
            logger.info("Auto data skipped.");
            return;
        }

        {
            //搜索所有entity对象，并自动进行自增初始化值设
            for (String managedClassName : entityManagerFactory.getPersistenceUnitInfo().getManagedClassNames()) {
                try {
                    Class<?> entityClass = Class.forName(managedClassName);
                    MetaData metaData = entityClass.getAnnotation(MetaData.class);
                    if (metaData != null && metaData.autoIncrementInitValue() > 0) {
                        autoIncrementInitValue(entityClass, metaData);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("class convert error", e);
                }
            }
        }

        CountThread countThread = new CountThread();
        countThread.start();

        for (AbstractDatabaseDataInitializeProcessor initializeProcessor : initializeProcessors) {
            logger.debug("Invoking data initialize for {}", initializeProcessor);
            countThread.update(initializeProcessor.getClass());

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        initializeProcessor.initialize(entityManager);
                    } catch (Exception e) {
                        throw new ServiceException("DatabaseDataInitialize Error", e);
                    }
                }
            });
        }

        //停止计数线程
        countThread.shutdown();
        //清空释放所有基础和模拟数据操作缓存
        entityManager.clear();
    }

    /**
     * 初始化自增对象起始值
     */
    private void autoIncrementInitValue(final Class<?> entity, MetaData metaData) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        if (Integer.valueOf(String.valueOf(count)) > 0) {
            logger.debug("Skipped autoIncrementInitValue as exist data: {}", entity.getClass());
            return;
        }
        Session session = entityManager.unwrap(Session.class);
        session.doWork((connection) -> {
            Table table = entity.getAnnotation(Table.class);
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
        });
    }
}

class CountThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(CountThread.class);

    private Class<?> runnerClass;
    private boolean running = true;
    private LocalDateTime start = LocalDateTime.now();

    @Override
    public void run() {
        while (running) {
            try {
                if (runnerClass != null) {
                    LocalDateTime now = LocalDateTime.now();
                    logger.info("Running at " + runnerClass.getName() + ". Total passed time "
                            + ChronoUnit.SECONDS.between(start, now) + " seconds at Thread: "
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
