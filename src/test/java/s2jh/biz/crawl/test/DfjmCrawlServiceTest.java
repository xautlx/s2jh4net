package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;

import org.junit.Test;

public class DfjmCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        crawlService.startup(buildDefaultCrawlConfig().setForceReparse(true), "http://www.dfjmw.cn/search/item/?id=15407");
        Thread.sleep(300000);
    }
}
