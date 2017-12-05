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

    //以请求线程为分界
    private static final ThreadLocal<String> datasourceHolder = new ThreadLocal<String>();

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
        String ds = (String) datasourceHolder.get();
        //立即移除，避免共享线程池导致多线程数据干扰
        datasourceHolder.remove();
        return ds;
    }
}
