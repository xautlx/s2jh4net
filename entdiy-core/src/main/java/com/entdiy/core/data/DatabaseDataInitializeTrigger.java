/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.data;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据库数据初始化处理器触发器
 */
@Component
public class DatabaseDataInitializeTrigger {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeTrigger.class);

    @Autowired
    private DatabaseDataInitializeExecutor databaseDataInitializeExecutor;

    @PostConstruct
    public void initialize() {
        try {
            databaseDataInitializeExecutor.initialize();
        } catch (Exception e) {
            String msg = null;
            Throwable msgException = e;
            do {
                msg = msgException.getMessage();
                msgException = msgException.getCause();
            } while (msgException != null);

            if (msg != null && msg.indexOf("Transaction not active") > -1) {
                logger.warn(msg);
            } else {
                logger.warn(e.getMessage(), e);
            }
        }

    }

}
