package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.filter.WebSiteContactInfoStartHtmlParseFilter;
import lab.s2jh.module.crawl.service.test.CrawlServiceTest;
import lab.s2jh.module.crawl.vo.CrawlConfig;
import lab.s2jh.module.crawl.vo.Outlink;

import org.junit.Test;

import com.google.common.collect.Sets;

public class WebSiteContactInfoCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        String url = "http://www.086080.com/";
        CrawlConfig crawlConfig = buildDefaultCrawlConfig().setForceRefetch(true);
        Outlink outlink = new Outlink();
        outlink.setUrl(url);
        outlink.setCrawlParseFilters(Sets.newHashSet(WebSiteContactInfoStartHtmlParseFilter.class.getName()));
        crawlService.injectOutlink(outlink, crawlConfig, url);
        crawlService.startup(crawlConfig, url).get();
    }
}
