package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;

import org.junit.Test;

public class ExpoCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        crawlService.startup(buildDefaultCrawlConfig().setForceReparse(true).setForceRefetch(true), "http://www.expo-china.com/");
        Thread.sleep(300000);
    }
}
