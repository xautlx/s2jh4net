/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.sql.DatabaseMetaData;

@Service
public class DatabaseDataInitializeService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeExecutor.class);

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public void autoIncrementInitValue() {
        logger.debug("Invoking autoIncrementInitValue...");
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


    /**
     * 初始化自增对象起始值
     */
    private void autoIncrementInitValue(final Class<?> entity, MetaData metaData) {
        if (metaData.autoIncrementInitValue() <= 1) {
            return;
        }
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
