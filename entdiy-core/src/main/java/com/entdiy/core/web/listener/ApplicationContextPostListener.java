package com.entdiy.core.web.listener;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.collect.Maps;

/**
 * Spring容器加载“之后”的ServletContextListener
 */
public class ApplicationContextPostListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContextPostListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.debug("Invoke ApplicationContextPostListener contextInitialized");
        try {
            ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

            SpringContextHolder.setApplicationContext(applicationContext);

            ServletContext sc = event.getServletContext();
            String appName = sc.getServletContextName();
            logger.info("[{}] init context ...", appName);

            Map<String, Object> globalConstant = Maps.newHashMap();
            sc.setAttribute("cons", globalConstant);
            globalConstant.put("booleanLabelMap", GlobalConstant.booleanLabelMap);
        } catch (Exception e) {
            logger.error("error detail:", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.debug("Invoke ApplicationContextPostListener contextDestroyed");
    }
}
