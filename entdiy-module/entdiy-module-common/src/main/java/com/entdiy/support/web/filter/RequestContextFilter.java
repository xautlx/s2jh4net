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
package com.entdiy.support.web.filter;

import com.entdiy.core.cons.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestContextFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    public void init(FilterConfig arg0) {
        logger.debug("Invoking RequestContextFilter init method...");
    }

    @Override
    public void destroy() {
        logger.debug("Invoking RequestContextFilter destroy method...");
    }

    /** 标识APP端语言选项，形如en-US|zh-CN|zh-TW|ja-JP等，用于服务端必要的国际化处理 */
    private static ThreadLocal<String> requestLocale = new NamedThreadLocal("RequestLocale");

    public static String getRequestLocale() {
        return requestLocale.get();
    }

    /** 前期通过过滤器把完整URL前缀组装好，以便在后续业务层使用 */
    private static String REQUEST_FULL_CONTEXT_URL;

    public static String getFullContextURL() {
        return REQUEST_FULL_CONTEXT_URL;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (HttpMethod.GET.name().equalsIgnoreCase(req.getMethod())) {
            requestLocale.set(req.getHeader(GlobalConstant.APP_LOCALE));
        }

        if (REQUEST_FULL_CONTEXT_URL == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(req.getScheme()).append("://").append(req.getServerName());
            sb.append(req.getServerPort() == 80 ? "" : ":" + req.getServerPort());
            sb.append(req.getContextPath());
            REQUEST_FULL_CONTEXT_URL = sb.toString();
            logger.info("Init REQUEST_FULL_CONTEXT_URL: {}", REQUEST_FULL_CONTEXT_URL);
        }

        chain.doFilter(request, reponse);

        //过滤完毕后，清空当前请求线程设置数据，避免在线程池环境干扰
        requestLocale.remove();
    }
}
