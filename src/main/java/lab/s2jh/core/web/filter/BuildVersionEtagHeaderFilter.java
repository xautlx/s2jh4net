package lab.s2jh.core.web.filter;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.support.service.DynamicConfigService;

import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * 继承  @see org.springframework.web.filter.ShallowEtagHeaderFilter 实现一个基于构建版本生成ETag的过滤器
 * 主要用于js，css等静态资源文件控制，方便与开发和生产阶段进行不同程度的缓存控制
 */
public class BuildVersionEtagHeaderFilter extends ShallowEtagHeaderFilter {

    protected String generateETagHeaderValue(byte[] bytes) {
        String responseETag = null;
        if (DynamicConfigService.isDevMode()) {
            //开发模式每次都添加不同时间戳以便随时获取最新代码
            responseETag = String.valueOf(new Date().getTime());
        } else {
            //生成模式取构建版本，每次更新版本后都能通知浏览器更新获取最新代码
            responseETag = DynamicConfigService.getBuildVersion();
        }
        return responseETag;
    }

    protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response,
            int responseStatusCode, byte[] responseBody) {
        return true;
    }
}
