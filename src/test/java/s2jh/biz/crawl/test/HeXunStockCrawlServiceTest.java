package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;

import org.junit.Test;

public class HeXunStockCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        crawlService
                .startup(buildDefaultCrawlConfig(),
                        "http://stockdata.stock.hexun.com/gszl/data/jsondata/ggml.ashx?no=300494&type=003&count=300&titType=null&page=1&callback=hxbase_json25")
                .get();
    }
}
