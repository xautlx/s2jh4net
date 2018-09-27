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
package com.entdiy.core.web.filter;

import com.entdiy.core.web.util.ServletUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 打印输出HTTP请求信息，一般用于开发调试
 * 生产环境把日志级别设定高于INFO即可屏蔽调试信息输出
 */
public class HttpRequestLogFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogFilter.class);

    @Override
    public void init(FilterConfig arg0) {
        logger.debug("Invoking HttpRequestLogFilter init method...");
    }

    @Override
    public void destroy() {
        logger.debug("Invoking HttpRequestLogFilter destroy method...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;

            String uri = req.getRequestURI();
            //静态资源直接跳过
            if (uri == null || uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".gif") || uri.endsWith(".png") || uri.endsWith(".jpg")
                    || uri.endsWith(".woff") || uri.endsWith(".woff2") || uri.endsWith(".ico") || uri.endsWith(".mp3")) {
                chain.doFilter(request, reponse);
                return;
            }

            //提取verbose参数标识是否开启详细信息输出
            boolean verbose = logger.isTraceEnabled() || BooleanUtils.toBoolean(req.getParameter("verbose"));

            Map<String, String> dataMap = ServletUtils.buildRequestInfoDataMap(req, verbose);
            StringBuilder sb = new StringBuilder("HTTP Request Info:");
            for (Map.Entry<String, String> me : dataMap.entrySet()) {
                sb.append(StringUtils.rightPad("\n" + me.getKey(), 50) + " : " + me.getValue());
            }
            logger.debug(sb.toString());
        }
        chain.doFilter(request, reponse);
    }
}
