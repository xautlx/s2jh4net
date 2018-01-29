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
package com.entdiy.core.dao.router;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RoutingDataSourceAdvice {

    private static final Logger logger = LoggerFactory.getLogger(RoutingDataSourceAdvice.class);

    private static final ThreadLocal<String> datasourceHolder = new ThreadLocal();

    @Before("@annotation(com.entdiy.core.dao.router.RoutingDataSource)")
    private void routingAdvice(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        RoutingDataSource datasource = methodSignature.getMethod().getAnnotation(RoutingDataSource.class);
        String customerType = datasource.value();
        logger.debug(" Routing to Datasource: {}", customerType);
        datasourceHolder.set(customerType);
    }

    public static void setSlaveDatasource() {
        logger.debug(" Routing to Datasource: {}", "slave");
        datasourceHolder.set("slave");
    }

    public static String getDatasource() {
        String ds = datasourceHolder.get();
        //立即移除，避免共享线程池导致多线程数据干扰
        datasourceHolder.remove();
        return ds;
    }
}
