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
package com.entdiy.security;

import com.entdiy.aud.entity.AccountLogonLog;
import com.entdiy.aud.service.AccountLogonLogService;
import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通过监听器更新相关登录记录的登录时间
 */
public class AuthLogonHistRefreshListener implements HttpSessionListener, ServletContextListener {

    private final static Logger logger = LoggerFactory.getLogger(AuthLogonHistRefreshListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //Do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        //基于Shiro判断session是否已登录过，由于未提供public常量访问因此直接参考HttpServletSession取代码中字符串
        if (session.getAttribute("org.apache.shiro.web.session.HttpServletSession.HOST_SESSION_KEY") != null) {
            String sessionId = session.getId();
            AccountLogonLogService accountLogonLogService = SpringContextHolder.getBean(AccountLogonLogService.class);
            AccountLogonLog accountLogonLog = accountLogonLogService.findBySessionId(sessionId);
            if (accountLogonLog != null) {
                logger.debug("Setup logout time for session ID: {}", sessionId);
                accountLogonLog.setLogoutTime(DateUtils.currentDateTime());
                Duration duration = Duration.between(accountLogonLog.getLogonTime(), accountLogonLog.getLogoutTime());
                accountLogonLog.setLogonTimeLength(duration.getSeconds());
                accountLogonLogService.save(accountLogonLog);
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //Do nothing
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //在容器销毁时把未正常结束遗留的登录记录信息强制设置登出时间
        logger.info("ServletContext destroy force setup session user logout time...");

        AccountLogonLogService accountLogonLogService = SpringContextHolder.getBean(AccountLogonLogService.class);
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultAndGroupFilter();
        groupPropertyFilter.append(new PropertyFilter(MatchType.NU, "logoutTime", Boolean.TRUE));
        List<AccountLogonLog> accountLogonLogs = accountLogonLogService.findByFilters(groupPropertyFilter);
        if (!CollectionUtils.isEmpty(accountLogonLogs)) {
            LocalDateTime yesterday = DateUtils.currentDateTime().minusDays(1);
            for (AccountLogonLog accountLogonLog : accountLogonLogs) {
                //超过一天都没有登出的，直接强制设置登出时间
                if (accountLogonLog.getLogonTime().isBefore(yesterday)) {
                    accountLogonLog.setLogoutTime(accountLogonLog.getLogonTime().plusHours(1));

                    logger.debug(" - Setup logout time for session ID: {}", accountLogonLog.getHttpSessionId());
                    Duration duration = Duration.between(accountLogonLog.getLogonTime(), accountLogonLog.getLogoutTime());
                    accountLogonLog.setLogonTimeLength(duration.getSeconds());
                    accountLogonLogService.save(accountLogonLog);
                }
            }
        }
    }
}
