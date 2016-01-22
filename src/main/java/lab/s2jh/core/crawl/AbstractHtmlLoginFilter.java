package lab.s2jh.core.crawl;

import java.util.regex.Pattern;

import lab.s2jh.module.crawl.service.CrawlService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author EMAIL:s2jh-dev@hotmail.com , QQ:2414521719
 *
 */
public abstract class AbstractHtmlLoginFilter implements CrawlLoginFilter {

    protected static final Logger logger = LoggerFactory.getLogger("crawl.login");

    private Pattern filterPattern;

    protected CrawlService crawlService;

    private Boolean loginSuccessed;

    /**
     * 用于自定义过滤器时强制设置
     * @param filterPattern
     */
    public void setFilterPattern(Pattern filterPattern) {
        this.filterPattern = filterPattern;
    }

    /**
     * 判断url是否符合自定义解析匹配规则
     * @param url
     * @return
     */
    private boolean isUrlMatchedForParse(String url) {
        if (filterPattern == null) {
            String regex = getUrlFilterRegex();
            if (StringUtils.isBlank(regex)) {
                return false;
            }
            filterPattern = Pattern.compile(regex);
        }
        if (filterPattern.matcher(url).find()) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean filter(String url) {
        try {
            //URL匹配
            if (!isUrlMatchedForParse(url)) {
                logger.trace("Skipped {} as not match regex [{}]", this.getClass().getName(), getUrlFilterRegex());
                return null;
            }
            if (loginSuccessed == null || loginSuccessed == Boolean.FALSE) {
                logger.info("Invoking login  {} for url: {}", this.getClass().getName(), url);
                loginSuccessed = filterInternal(url);
                if (Boolean.TRUE.equals(loginSuccessed)) {
                    logger.info("Login SUCCESS for url: {}", url);
                } else {
                    logger.info("Login FAILURE for url: {}", url);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return loginSuccessed;
    }

    /**
     * 设置当前解析过滤器匹配的URL正则表达式
     * 只有匹配的url才调用当前解析处理逻辑
     * @return
     */
    protected abstract String getUrlFilterRegex();

    /**
     * 子类实现具体的页面数据解析逻辑
     * @return
     */
    public abstract Boolean filterInternal(String url) throws Exception;

}
