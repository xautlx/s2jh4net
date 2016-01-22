package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;

import org.junit.Test;

public class B2bdqCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        crawlService.startup(buildDefaultCrawlConfig(), "http://www.b2bdq.com").get();
    }
}
