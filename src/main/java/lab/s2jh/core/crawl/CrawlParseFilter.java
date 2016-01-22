package lab.s2jh.core.crawl;

import java.util.regex.Pattern;

import lab.s2jh.module.crawl.service.CrawlService;
import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public interface CrawlParseFilter {

    public final static String URL = "URL";

    public final static String ID = "业务标识";

    public final static String PARSE_INLINK_URL = "注入来源";

    public final static String PARSE_FROM_URLS = "解析来源";

    public final static String SITE_NAME = "站点分组";

    public final static String PAGE_TITLE = "页面标题";

    public final static String OUTLINKS = "outlinks";

    /**
     * 用于自定义过滤器时动态设置
     * @param filterPattern
     */
    void setFilterPattern(Pattern filterPattern);

    void setCrawlService(CrawlService crawlService);

    DBObject filter(String url, WebPage webPage) throws Exception;
}
