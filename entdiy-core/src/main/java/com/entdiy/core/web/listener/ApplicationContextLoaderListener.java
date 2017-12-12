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
package com.entdiy.core.web.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

/**
 * 简单扩展Spring标准的ContextLoaderListener，以便兼容共享jar部署模式
 */
public class ApplicationContextLoaderListener extends ContextLoaderListener {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
            logger.info("Using ClassLoader[{}]: {}", originalLoader.hashCode(), originalLoader);
        }
        super.contextInitialized(event);
    }

}
