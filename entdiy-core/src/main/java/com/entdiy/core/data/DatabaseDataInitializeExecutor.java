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

import com.entdiy.core.web.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private DatabaseDataInitializeService databaseDataInitializeService;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private List<AbstractDatabaseDataInitializeProcessor> initializeProcessors;

    @PostConstruct
    public void initialize() {
        //先清空缓存数据
        if (AppContextHolder.isDevMode()) {
            entityManager.getEntityManagerFactory().getCache().evictAll();
        }

        //对@Table注解的自增扩展属性处理
        databaseDataInitializeService.autoIncrementInitValue();

        CountThread countThread = new CountThread();
        countThread.start();

        for (AbstractDatabaseDataInitializeProcessor initializeProcessor : initializeProcessors) {
            logger.debug("Invoking data initialize for {}", initializeProcessor);
            countThread.update(initializeProcessor.getClass());

            initializeProcessor.initialize();
        }

        //停止计数线程
        countThread.shutdown();
        //清空释放所有基础和模拟数据操作缓存
        entityManager.clear();
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
