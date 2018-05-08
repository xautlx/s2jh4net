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
package com.entdiy.support.web;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.locale.web.RequestLocaleHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (HttpMethod.GET.name().equalsIgnoreCase(req.getMethod())) {
            RequestLocaleHolder.setRequestLocale(req.getHeader(GlobalConstant.APP_LOCALE));
        }

        chain.doFilter(request, reponse);

        //过滤完毕后，清空当前请求线程设置数据，避免在线程池环境干扰
        RequestLocaleHolder.clear();
    }
}
