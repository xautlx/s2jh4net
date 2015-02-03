/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.service.GlobalConfigService;
import lab.s2jh.core.web.util.ServletUtils;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印输出HTTP请求信息，一般用于开发调试
 * 生产环境把日志级别设定高于INFO即可屏蔽调试信息输出
 */
public class HttpRequestLogFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.debug("Invoking HttpRequestLogFilter init method...");
    }

    @Override
    public void destroy() {
        logger.debug("Invoking HttpRequestLogFilter destroy method...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException,
            ServletException {
        if (logger.isInfoEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI();
            //静态资源直接跳过
            if (uri == null || uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".gif")
                    || uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".woff") || uri.endsWith(".ico")) {
                chain.doFilter(request, reponse);
                return;
            }

            //如果是开发模式则开启debug
            boolean debug = GlobalConfigService.isDevMode();
            if (debug == false) {
                //如果是非开发模式，提取请求参数标识开启参数debug
                debug = BooleanUtils.toBoolean(req.getParameter("debug"));
            }
            if (debug) {
                //在debug模式下，再提取verbose参数标识是否开启详细信息输出
                boolean verbose = BooleanUtils.toBoolean(req.getParameter("verbose"));
                logger.info(ServletUtils.buildRequestInfoToString(req, BooleanUtils.toBoolean(verbose)));
            }
        }
        chain.doFilter(request, reponse);
    }
}
