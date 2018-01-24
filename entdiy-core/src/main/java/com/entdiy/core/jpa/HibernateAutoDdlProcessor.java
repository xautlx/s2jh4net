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
package com.entdiy.core.jpa;

import com.entdiy.core.web.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * 对hibernate.hbm2ddl.auto参数进行配置校验，防止在生产模式错误配置导致极度危险的数据删除操作
 */
@Component
public class HibernateAutoDdlProcessor {

    private static Logger logger = LoggerFactory.getLogger(HibernateAutoDdlProcessor.class);

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddl;

    @PostConstruct
    public void assertHbm2ddl() {
        logger.debug("Assert hibernate hbm2ddl auto: {}", hbm2ddl);
        //必须是开发模式才允许删除数据库
        if ("create-drop".equalsIgnoreCase(hbm2ddl)) {
            logger.error("Hibernate hbm2ddl auto: {} NOT allowed at NO dev mode.", hbm2ddl);
            Assert.isTrue(AppContextHolder.isDevMode(), "Hibernate hbm2ddl auto: " + hbm2ddl + " NOT allowed.");
            System.exit(0);
        }
    }
}
