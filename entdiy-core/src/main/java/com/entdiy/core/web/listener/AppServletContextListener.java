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
package com.entdiy.core.web.listener;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.web.AppContextHolder;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

public class AppServletContextListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(AppServletContextListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Invoking AppServletContextListener contextInitialized");

        ServletContext sc = sce.getServletContext();
        //Spring上下文帮助类设置
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContextHolder.setApplicationContext(applicationContext);

        //对应用相关静态支持数据初始化
        AppContextHolder.init(sc);

        //设置一些全局属性，便于JSP页面直接EL表达式获取
        String contextPath = sc.getContextPath();
        logger.debug("App running with contextPath: {}", contextPath);
        sc.setAttribute("ctx", "/".equals(contextPath) ? "" : contextPath);
        sc.setAttribute("devMode", AppContextHolder.isDevMode());

        Map<String, Object> globalConstant = Maps.newHashMap();
        sc.setAttribute("cons", globalConstant);
        globalConstant.put("booleanLabelMap", GlobalConstant.booleanLabelMap);

        String appName = sc.getServletContextName();
        logger.info("[{}] context initialized.", appName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Do nothing
    }


}
