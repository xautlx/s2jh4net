package lab.s2jh.module.crawl.service.test;

import java.util.Date;

import javax.annotation.PostConstruct;

import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.module.crawl.service.CrawlService;
import lab.s2jh.module.crawl.vo.CrawlConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:/context/context-profiles.xml", "classpath:/context/spring-mongo.xml",
        "classpath:/service/spring-crawl.xml" })
public abstract class CrawlServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected CrawlService crawlService;

    @PostConstruct
    public void init() {
        SpringContextHolder.setApplicationContext(applicationContext);
    }

    protected CrawlConfig buildDefaultCrawlConfig() {
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setForceReparse(true);
        crawlConfig.setBatchId(new Date().getTime());
        return crawlConfig;
    }
}
