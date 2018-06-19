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
import com.entdiy.core.web.AppContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

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

    /**
     * 返回形如en-US|zh-CN|zh-TW|ja-JP的当前国际化标识
     *
     * @return
     */
    public static String getRequestLocale() {
        String locale = requestLocale.get();
        //如果未指定，默认取不为空约束的中文属性值
        locale = StringUtils.isBlank(locale) ? "zh-CN" : locale;
        return locale;
    }

    /**
     * 返回形如enUS|zhCN|zhTW|jaJP的当前国际化对应Java属性名称
     *
     * @return
     */
    public static String getRequestLocalePropName() {
        String locale = getRequestLocale();
        //移除中横线以匹配对应属性名称
        locale = StringUtils.remove(locale, "-");
        return locale;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (HttpMethod.GET.name().equalsIgnoreCase(req.getMethod())) {
            requestLocale.set(req.getHeader(GlobalConstant.APP_LOCALE));
        }

        String webContextUri = AppContextHolder.getWebContextUri();
        if (webContextUri == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(req.getScheme()).append("://").append(req.getServerName());
            sb.append(req.getServerPort() == 80 ? "" : ":" + req.getServerPort());
            sb.append(req.getContextPath());
            webContextUri = sb.toString();
            AppContextHolder.setWebContextUri(webContextUri);
        }

        chain.doFilter(request, reponse);

        //过滤完毕后，清空当前请求线程设置数据，避免在线程池环境干扰
        requestLocale.remove();
    }
}
