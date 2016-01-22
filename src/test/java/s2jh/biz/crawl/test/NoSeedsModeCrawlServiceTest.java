package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;
import lab.s2jh.module.crawl.vo.CrawlConfig;

import org.junit.Test;

public class NoSeedsModeCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        CrawlConfig crawlConfig = buildDefaultCrawlConfig();
        crawlConfig.setFetchMinInterval(1);
        crawlConfig.setThreadNum(1);
        crawlService.startup(crawlConfig).get();
    }
}
