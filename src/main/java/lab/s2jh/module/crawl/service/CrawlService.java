package lab.s2jh.module.crawl.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import lab.s2jh.core.crawl.CrawlLoginFilter;
import lab.s2jh.core.crawl.CrawlParseFilter;
import lab.s2jh.core.dao.mongo.MongoDao;
import lab.s2jh.core.util.ChineseUtils;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.core.util.ThreadUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.crawl.vo.CrawlConfig;
import lab.s2jh.module.crawl.vo.Outlink;
import lab.s2jh.module.crawl.vo.WebPage;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.util.ClassUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 核心的爬虫抓取解析服务接口。参考Apache Nutch相似的设计实现思路，具体可参考官方Nutch官方资料。
 * 可参考 @see https://github.com/xautlx/nutch-ajax/blob/master/document/Apache_Nutch_Solr_Solution_with_AJAX_support.md
 */
@Service
public class CrawlService {

    private static final Logger logger = LoggerFactory.getLogger("crawl.service");

    /** 为了简化查询避免null值处理，日期相关字段初始化值日期起点 */
    private Date INIT_DATE = new DateTime(0).toDate();

    /** fetch，parse等失败后，最大尝试次数，如果计数器超过此值则不再继续尝试抓取或解析 */
    private static final int RETRY_TIMES = 5;

    /** 302最大转向请求次数 */
    private static final int MAX_REDIRECT_TIMES = 5;

    @Autowired
    private MongoDao mongoDao;

    /** 爬虫主线程开始执行时间 */
    private Date crawlStartTime;

    /** 总计抓取解析的页面数量计数器 */
    private AtomicInteger pages = new AtomicInteger(0);

    /** 抓取解析线程执行标志 */
    private boolean running;

    /** 抓取解析线程池执行器 */
    private ThreadPoolTaskExecutor executor;

    /** 默认的User-Agent请求头信息 */
    public final static String User_Agent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";

    public final static String Crawl_Fetch_Data_Collection = "crawl_fetch_data";

    public final static String Crawl_Parse_Data_Collection = "crawl_parse_data";

    public final static String Crawl_Failure_Data_Collection = "crawl_failure_data";

    public final static String Default_Charset_UTF8 = "utf-8";

    /** 单例的HTTP Client请求对象 */
    private static CloseableHttpClient httpClientInstance;
    /** 单例的HTTP Client连接池管理器对象 */
    private static PoolingHttpClientConnectionManager poolConnManager;

    public static synchronized CloseableHttpClient buildHttpClient() {
        if (httpClientInstance == null) {
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(100000).setSocketTimeout(100000).setConnectTimeout(100000)
                    .setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false).build();

            poolConnManager = new PoolingHttpClientConnectionManager();
            //总计最大并发连接数
            poolConnManager.setMaxTotal(300);
            //单个站点最大并发连接数
            poolConnManager.setDefaultMaxPerRoute(100);

            httpClientInstance = HttpClients.custom().setConnectionManager(poolConnManager).setDefaultRequestConfig(config).build();
        }
        if (logger.isDebugEnabled()) {
            if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
                logger.debug("HttpClient pool stats: " + poolConnManager.getTotalStats().toString());
            }
        }
        return httpClientInstance;
    }

    /** 页面解析过滤器集合 */
    private List<CrawlParseFilter> crawlParseFilters;

    public synchronized List<CrawlParseFilter> buildCrawlParseFilters() {
        if (crawlParseFilters == null) {
            crawlParseFilters = Lists.newArrayList();

            //基于Spring的继承规则获取所有解析过滤器集合列表
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            //实现CrawlParseFilter接口
            scan.addIncludeFilter(new AssignableTypeFilter(CrawlParseFilter.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**"));

            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> beanClass = ClassUtils.forName(beanDefinition.getBeanClassName());
                CrawlParseFilter crawlParseFilter = (CrawlParseFilter) ClassUtils.newInstance(beanClass);
                crawlParseFilter.setCrawlService(this);
                crawlParseFilters.add(crawlParseFilter);
            }
        }
        return crawlParseFilters;
    }

    /** 登录处理过滤器集合 */
    private List<CrawlLoginFilter> crawlLoginFilters;

    private synchronized List<CrawlLoginFilter> buildCrawlLoginFilters() {
        if (crawlLoginFilters == null) {
            crawlLoginFilters = Lists.newArrayList();

            //基于Spring的继承规则获取所有解析过滤器集合列表
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            //实现CrawlLoginFilter接口
            scan.addIncludeFilter(new AssignableTypeFilter(CrawlLoginFilter.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**"));

            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> beanClass = ClassUtils.forName(beanDefinition.getBeanClassName());
                CrawlLoginFilter crawlLoginFilter = (CrawlLoginFilter) ClassUtils.newInstance(beanClass);
                crawlLoginFilters.add(crawlLoginFilter);
            }
        }
        return crawlLoginFilters;
    }

    /**
     * 获取爬取数据集合对象实例
     * @return
     */
    private DBCollection buildFetchObjectCollection() {
        return mongoDao.getDB().getCollection(Crawl_Fetch_Data_Collection);
    }

    /**
     * 获取解析数据集合对象实例
     * @return
     */
    private DBCollection buildParseObjectCollection() {
        return mongoDao.getDB().getCollection(Crawl_Parse_Data_Collection);
    }

    /**
     * 获取通知数据集合对象实例
     * @return
     */
    private DBCollection buildFailureObjectCollection() {
        return mongoDao.getDB().getCollection(Crawl_Failure_Data_Collection);
    }

    /**
     * 构造inject注入对象
     * @param dbColl
     * @param url
     * @param crawlConfig
     * @return
     */
    private DBObject buildInjectDBObject(DBCollection dbColl, String url, CrawlConfig crawlConfig) {
        //清洗#后面无意义字符
        url = StringUtils.substringBefore(url, "#").trim();
        //构造用于$set操作的数据对象
        DBObject item = new BasicDBObject(CrawlParseFilter.URL, url);
        if (dbColl.findOne(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject(CrawlParseFilter.URL, 1)) == null) {
            item.put("generateTime", INIT_DATE);
            item.put("fetchTouchTime", INIT_DATE);
            item.put("parseTime", null);
            item.put("fetchTime", null);
            item.put("httpStatus", -1);
            item.put("fetchFailureTimes", 0);
        }
        item.put("injectTime", new Date());
        //每次启动抓取线程作为一个批次，避免互相干扰
        item.put("batchId", crawlConfig.getBatchId());
        return item;
    }

    private void injectSeeds(String url, CrawlConfig crawlConfig) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        DBCollection dbColl = buildFetchObjectCollection();
        DBObject item = buildInjectDBObject(dbColl, url, crawlConfig);
        item.put("injectSeed", true);
        item.put("sourceUrl", url);
        //种子URL强制重新抓取解析
        item.put("fetchTime", null);
        item.put("parseTime", null);
        logger.info("Inject Seed: {} ", url);
        dbColl.update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", item), true, false);
    }

    public void injectOutlink(Outlink outlink, CrawlConfig crawlConfig, String sourceUrl) {
        String url = outlink.getUrl();
        if (StringUtils.isBlank(url)) {
            return;
        }
        DBCollection dbColl = buildFetchObjectCollection();
        DBObject item = buildInjectDBObject(dbColl, url, crawlConfig);
        item.put("injectSeed", false);
        item.put("sourceUrl", sourceUrl);
        //指明outlink特定解析器处理
        if (outlink.getCrawlParseFilters() != null) {
            item.put("crawlParseFilters", outlink.getCrawlParseFilters());
        }
        //初始化outlink的分组标识
        if (outlink.getBizSiteName() != null) {
            item.put("bizSiteName", outlink.getBizSiteName());
        }
        //初始化outlink的分组ID标识
        if (outlink.getBizId() != null) {
            item.put("bizId", outlink.getBizId());
        }
        if (outlink.getTitle() != null) {
            item.put("title", outlink.getTitle());
        }
        //标识此outlink属于强制解析类型，如动态的分页URL
        if (outlink.isForceRefetch()) {
            //强制重新抓取解析
            item.put("fetchTime", null);
            item.put("parseTime", null);
        }
        logger.info("Inject Outlink: {} ", outlink);
        dbColl.update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", item), true, false);

        //初始化outlink解析数据
        if (outlink.getParsedDBObject() != null) {
            DBObject update = new BasicDBObject();
            update.put(CrawlParseFilter.PARSE_INLINK_URL, sourceUrl);
            update.putAll(outlink.getParsedDBObject());
            saveParseDBObject(outlink.getUrl(), outlink.getBizSiteName(), outlink.getBizId(), update);
        }
    }

    public void saveParseDBObject(String url, String siteName, String bizId, DBObject update) {
        Assert.notNull(siteName, "解析数据保存 " + CrawlParseFilter.SITE_NAME + " 属性值不能为空");
        Assert.notNull(bizId, "解析数据保存 " + CrawlParseFilter.ID + " 属性值不能为空");

        update.put("解析时间", new Date());
        update.put(CrawlParseFilter.SITE_NAME, siteName);
        update.put(CrawlParseFilter.ID, bizId);

        BasicDBObject query = new BasicDBObject("$and", new BasicDBObject[] { new BasicDBObject(CrawlParseFilter.SITE_NAME, siteName),
                new BasicDBObject(CrawlParseFilter.ID, bizId) });

        synchronized (this) {
            DBObject parsedDBObject = buildParseObjectCollection().findOne(query);

            //多个页面解析数据合并为一条业务数据记录，合并计算业务数据来源URL地址列表
            if (parsedDBObject == null) {
                update.put(CrawlParseFilter.PARSE_FROM_URLS, url);
            } else {
                Object urls = parsedDBObject.get(CrawlParseFilter.PARSE_FROM_URLS);
                Set<Object> parsedFromUrls = Sets.newLinkedHashSet();
                if (urls instanceof BasicDBList) {
                    parsedFromUrls.addAll(Sets.newHashSet(((BasicDBList) urls).iterator()));
                } else {
                    String preURLs = (String) parsedDBObject.get(CrawlParseFilter.PARSE_FROM_URLS);
                    parsedFromUrls.addAll(Sets.newHashSet(StringUtils.split(preURLs, "\n,")));
                }
                parsedFromUrls.add(url);
                update.put(CrawlParseFilter.PARSE_FROM_URLS, StringUtils.join(parsedFromUrls, ","));
            }

            logger.debug("Save Parsed Data for URL {} is: {}", url, update);
            buildParseObjectCollection().update(query, new BasicDBObject("$set", update), true, false);
        }
    }

    /**
     * 页面Fetch处理异常,则重新注入尝试下次重新fetch和parse,直到超过重试次数则终止
     * @param webPage
     */
    private void injectFetchFailureRetry(String url, int statusCode, String result, String fetchFailureException) {
        DBCollection dbColl = buildFetchObjectCollection();
        DBObject item = dbColl.findOne(new BasicDBObject(CrawlParseFilter.URL, url),
                new BasicDBObject(CrawlParseFilter.URL, 1).append("fetchFailureTimes", 1));

        Integer fetchFailureTimes = (Integer) item.get("fetchFailureTimes");
        if (fetchFailureTimes == null) {
            fetchFailureTimes = 0;
        }

        fetchFailureTimes = fetchFailureTimes + 1;
        DBObject update = new BasicDBObject();
        update.put("fetchFailureTimes", fetchFailureTimes);
        update.put("fetchFailureException", fetchFailureException);
        update.put("fetchTouchTime", new Date());
        update.put("httpStatus", statusCode);
        update.put("httpResponse", result);

        if (fetchFailureTimes > RETRY_TIMES) {
            logger.debug("Skipped fetch retry due to limit: {} , fetchFailureTimes: {}", url, fetchFailureTimes);
        } else {
            update.put("injectTime", new Date());
            update.put("generateTime", INIT_DATE);
            update.put("fetchTouchTime", INIT_DATE);
            update.put("parseTime", null);
            update.put("fetchTime", null);
            logger.info("Inject fetch retry: {} , fetchFailureTimes: {}", url, fetchFailureTimes);
        }
        buildFetchObjectCollection().update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", update));
    }

    /**
     * 页面调用Parse过滤器处理异常,则重新注入尝试下次重新fetch和parse,直到超过重试次数则终止
     * @param webPage
     */
    public void injectParseFailureRetry(WebPage webPage, String parseFailureException) {
        DBCollection dbColl = buildFetchObjectCollection();
        String url = webPage.getUrl();
        DBObject item = dbColl.findOne(new BasicDBObject(CrawlParseFilter.URL, url),
                new BasicDBObject(CrawlParseFilter.URL, 1).append("parseFailureTimes", 1));

        Integer parseFailureTimes = (Integer) item.get("parseFailureTimes");
        if (parseFailureTimes == null) {
            parseFailureTimes = 0;
        }

        parseFailureTimes = parseFailureTimes + 1;
        DBObject update = new BasicDBObject();
        update.put("parseFailureTimes", parseFailureTimes);
        update.put("parseFailureException", parseFailureException);

        if (parseFailureTimes > RETRY_TIMES) {
            logger.debug("Skipped parse retry due to limit: {} , parseFailureTimes: {}", url, parseFailureTimes);
        } else {
            update.put("injectTime", new Date());
            update.put("generateTime", INIT_DATE);
            update.put("fetchTouchTime", INIT_DATE);
            update.put("parseTime", null);
            update.put("fetchTime", null);
            logger.info("Inject parse retry: {} , parseFailureTimes: {}", url, parseFailureTimes);
        }

        buildFetchObjectCollection().update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", update));
    }

    /**
     * 更新生成待抓取URL集合列表
     * @param crawlConfig
     * @param seedMode
     * @return 
     */
    public synchronized int generator(CrawlConfig crawlConfig, boolean seedMode) {
        //为了避免过度消耗演示环境的宽带和存储资源，限制处理总数
        if (DynamicConfigService.isDemoMode() && pages.get() > 100) {
            logger.info("Skipped generator as running DEMO mode.");
            return 0;
        }

        DBCollection dbColl = buildFetchObjectCollection();

        //http://www.chaolv.com/about/contact.html  http://shkangdexin.b2b.hc360.com/    http://4001671615ylj.b2b.hc360.com/  http://huxinsheng1969.b2b.hc360.com/shop/show.html
        BasicDBObject query = null;
        if (seedMode) {
            //如果提供了种子URL启动执行，则始终取当前种子URL执行批次数据生成待抓取URL列表，并且排除超过抓取次数限制的
            query = new BasicDBObject("generateTime", new BasicDBObject("$lt", crawlConfig.getStartTime()));
            query.append("batchId", crawlConfig.getBatchId());
            query.append("fetchFailureTimes", new BasicDBObject("$lte", RETRY_TIMES));
        } else {
            //如果未提供种子URL启动执行，则取所有非200状态的URL列表作为待抓取URL列表，并且排除超过抓取次数限制的
            query = new BasicDBObject("generateTime", new BasicDBObject("$lt", crawlConfig.getStartTime()));
            query.append("$or", new BasicDBObject[] { new BasicDBObject("httpStatus", new BasicDBObject("$gt", 200)),
                    new BasicDBObject("httpStatus", new BasicDBObject("$lt", 200)) });
            query.append("fetchFailureTimes", new BasicDBObject("$lte", RETRY_TIMES));
        }

        DBCursor cur = dbColl.find(query, new BasicDBObject(CrawlParseFilter.URL, 1).append("httpStatus", 1).append("fetchFailureTimes", 1))
                .sort(new BasicDBObject("injectTime", 1)).limit(100);
        //logger.debug("MongoDB query explain: {}", cur.explain());

        int count = 0;
        while (running && cur.hasNext()) {
            DBObject item = cur.next();
            String url = (String) item.get(CrawlParseFilter.URL);
            DBObject update = new BasicDBObject();
            update.put("batchId", crawlConfig.getBatchId());
            update.put("generateTime", new Date());
            logger.info("Generator: {} , HttpStatus: {} ,fetchFailureTimes: {}", url, item.get("httpStatus"), item.get("fetchFailureTimes"));

            buildFetchObjectCollection().update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", update));
            count++;
        }
        return count;
    }

    /**
     * 提取待抓取URL列表，以多线程方式对URL进行抓取和解析处理
     * @param crawlConfig
     */
    public void fetcher(CrawlConfig crawlConfig) {
        DBCollection dbColl = buildFetchObjectCollection();

        BasicDBObject query = new BasicDBObject();
        //查询生成时间大于本次爬虫启动时间
        query.append("generateTime", new BasicDBObject("$gt", crawlConfig.getStartTime()));
        //查询当前批次
        query.append("batchId", crawlConfig.getBatchId());
        //查询抓取时间小于本次爬虫启动时间（排除本次已经爬过的记录）
        query.append("fetchTouchTime", new BasicDBObject("$lt", crawlConfig.getStartTime()));

        DBCursor cur = dbColl.find(query).sort(new BasicDBObject("generateTime", 1)).limit(100);
        //logger.debug("MongoDB query explain: {}", cur.explain());

        logger.debug("Thread pool executor stat: [{}/{}].", executor.getActiveCount(), executor.getCorePoolSize());
        while (running && cur.hasNext()) {
            DBObject item = cur.next();
            int activeThreads = executor.getActiveCount();
            //如果当前活动线程数饱和，则稍等空闲线程
            while (activeThreads >= executor.getCorePoolSize()) {
                ThreadUtils.sleepOneSecond();
                logger.info("Thread pool executor full [active: {}], waiting...", activeThreads);
                activeThreads = executor.getActiveCount();
            }
            //如果有空闲线程，则提交线程池处理
            executor.execute(new FetcherThread(this, item, crawlConfig));
        }
    }

    /**
     * 爬虫服务启动入口
     * @param crawlConfig
     * @param urls
     */
    public FutureTask<Integer> startup(final CrawlConfig crawlConfig, final String... urls) {
        running = true;

        //每次启动抓取线程作为一个批次，避免互相干扰
        if (crawlConfig.getBatchId() == null) {
            crawlConfig.setBatchId(new Date().getTime());
        }

        DBCollection coll = buildFetchObjectCollection();

        //为了提高查询效率，为爬取数据集合的相关属性添加索引,约束

        //URL唯一约束
        coll.createIndex(new BasicDBObject(CrawlParseFilter.URL, 1), new BasicDBObject("unique", true).append("background", true));

        //generator查询索引
        coll.createIndex(new BasicDBObject("generateTime", 1).append("batchId", 1).append("fetchFailureTimes", 1).append("injectTime", 1),
                new BasicDBObject("background", true));
        coll.createIndex(new BasicDBObject("generateTime", 1).append("httpStatus", 1).append("fetchFailureTimes", 1).append("injectTime", 1),
                new BasicDBObject("background", true));

        //fetcher查询索引
        coll.createIndex(new BasicDBObject("generateTime", 1).append("batchId", 1).append("fetchTouchTime", 1), new BasicDBObject("background", true));

        //为解析数据集合的相关属性添加索引,约束
        buildParseObjectCollection().createIndex(new BasicDBObject(CrawlParseFilter.SITE_NAME, 1).append(CrawlParseFilter.ID, 1),
                new BasicDBObject("unique", true).append("background", true));

        //为解析数据集合的相关属性添加索引,约束
        buildFailureObjectCollection().createIndex(new BasicDBObject(CrawlParseFilter.URL, 1),
                new BasicDBObject("unique", true).append("background", true));

        //初始化设置线程池参数
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(crawlConfig.getThreadNum());
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Crawl-");
        executor.initialize();

        logger.info("Prepare to startup crawl thread with config: {}", crawlConfig);
        crawlStartTime = new Date();

        FutureTask<Integer> future = new FutureTask<Integer>(new Runnable() {

            /** 异步执行主线程连续休眠秒数，如果超过一定阈值则退出主线程 */
            int sleepSeconds = 0;

            /** 记录最后一次生成待爬取URL列表的时间，控制两次generator操作之前保持一定的间隔，控制主线程循环节奏 */
            Date lastGenerateTime;

            @Override
            public void run() {
                //convertLongToDate("fetchTouchTime");

                //根据是否提供种子url集合设置标识参数
                boolean seedMode = true;
                if (urls != null && urls.length > 0) {
                    if (logger.isInfoEnabled()) {
                        for (String url : urls) {
                            logger.info(" - Seed URL: {}", url);
                        }
                    }
                    for (String url : urls) {
                        injectSeeds(url, crawlConfig);
                    }
                } else {
                    seedMode = false;
                    logger.info(" - NO Seed URL");
                }

                do {
                    Date now = new Date();
                    //控制两次generator操作之前保持一定的间隔，控制主线程循环节奏
                    if (lastGenerateTime != null && (now.getTime() - lastGenerateTime.getTime() < 2 * 1000)) {
                        ThreadUtils.sleepOneSecond();
                        continue;
                    }
                    lastGenerateTime = now;

                    //调用generator接口更新生成待爬取URL集合，返回影响记录数
                    int count = generator(crawlConfig, seedMode);
                    if (count == 0) {
                        //如果返回待爬取记录数为0，有可能是其他线程正在执行还未添加新的outlink，则短暂休眠等待
                        ThreadUtils.sleepOneSecond();
                        //并累加连续休眠计数器，如果达到一定阈值则说明已经没有新的URL需要处理，终止爬虫主线程
                        sleepSeconds++;
                        logger.info("Crawl thread sleep {} seconds for more generate URL.", sleepSeconds);
                    } else {
                        //如果生成了待爬取URL集合数据，则重置连续休眠计数器，并调用抓取接口
                        sleepSeconds = 0;
                        fetcher(crawlConfig);
                    }
                } while (running && sleepSeconds < 30);

                //重置计数器
                pages = new AtomicInteger(0);

                logger.info("Crawl thread terminated at {} , start from {}", new Date(), crawlStartTime);
            }
        }, null);

        //爬虫异步执行主线程
        new Thread(future).start();

        return future;
    }

    /**
     * 更新运行标志，通知强制终止爬虫主线程及其他所有抓取解析子线程
     */
    public void shutdown() {
        running = false;
    }

    /**
     * 抓取处理线程实现
     */
    private class FetcherThread implements Runnable {

        private CrawlService crawlService;

        private DBObject item;

        private CrawlConfig crawlConfig;

        public FetcherThread(CrawlService crawlService, DBObject item, CrawlConfig crawlConfig) {
            this.crawlService = crawlService;
            this.crawlConfig = crawlConfig;
            this.item = item;
        }

        /**
         * 对响应内容字符串转换做兼容处理，默认转换为utf-8，通过比对页面的charset信息进行合理的字符集转换处理
         * @param url
         * @param responseEntity
         * @return
         * @throws Exception
         */
        private String responseEntityToString(String url, HttpEntity responseEntity) throws Exception {
            byte[] responseBytes = EntityUtils.toByteArray(responseEntity);

            //从响应头及内容提取字符集信息
            String charset = null;
            final ContentType contentType = ContentType.get(responseEntity);
            if (contentType != null) {
                Charset contentTypeCharset = contentType.getCharset();
                if (contentTypeCharset != null) {
                    charset = contentTypeCharset.name();
                }
            }
            String result = null;
            if (StringUtils.isBlank(charset)) {
                result = new String(responseBytes, Default_Charset_UTF8);
            } else {
                result = new String(responseBytes, charset);
            }

            //<meta content="text/html; charset=gb2312" http-equiv="Content-Type">
            if (StringUtils.isBlank(charset)) {
                charset = StringUtils.substringBetween(result, "charset=", "\"");
            }
            //<meta charset="UTF-8">
            if (StringUtils.isBlank(charset)) {
                charset = StringUtils.substringBetween(result, "charset=\"", "\"");
            }
            if (StringUtils.isNotBlank(charset)) {
                charset = charset.trim().toLowerCase();
                if (!charset.equals(Default_Charset_UTF8.toLowerCase())) {
                    //有些页面的响应和meta元素并不一致，为了处理此种情况做一个title中文乱码判断，确认是乱码再进行转码
                    String title = StringUtils.substringBetween(result, "<title>", "</title>");
                    if (StringUtils.isNotBlank(title) && ChineseUtils.isMessyCode(title)) {
                        logger.info("HTML Charset convert from {} to {} for URL: {}", Default_Charset_UTF8, charset, url);
                        result = new String(responseBytes, charset);
                    }
                }
            }
            return result;
        }

        @Override
        public void run() {
            DBCollection fetchObjectCollection = buildFetchObjectCollection();
            String url = (String) item.get(CrawlParseFilter.URL);
            DBObject urlQuery = new BasicDBObject(CrawlParseFilter.URL, url);
            DBObject fetchUpdate = new BasicDBObject("fetchTouchTime", new Date());

            int statusCode = -1;
            String result = null;
            String exception = null;
            String redirectUrl = null;
            Integer httpStatus = (Integer) item.get("httpStatus");
            Date fetchTime = (Date) item.get("fetchTime");
            //如果没有抓取过，或者抓取失败，或者配置为强制重新抓取
            if (httpStatus == null || httpStatus != 200 || fetchTime == null || crawlConfig.isForceRefetch()) {
                logger.info("Fetching: {}", url);

                Date now = new Date();

                //有些网站限制连续访问的频率，可根据人工简单测试找到合理的爬取间隔值，控制两次URL请求之间保持足够的间隔时间
                if (crawlConfig.getFetchMinInterval() > 0) {
                    if (crawlConfig.getLastFetchTime() != null) {
                        int seconds = Long.valueOf((now.getTime() - crawlConfig.getLastFetchTime().getTime()) / 1000).intValue();
                        if (crawlConfig.getFetchMinInterval() > seconds) {
                            ThreadUtils.sleepSeconds(crawlConfig.getFetchMinInterval() - seconds);
                            logger.debug("Thread sleep {} seconds for submit FetcherThread according to crawlConfig.fetchMinInterval",
                                    crawlConfig.getFetchMinInterval());
                        }
                    }
                    crawlConfig.setLastFetchTime(now);
                }

                //首先基于登录过滤器集合，进行登录预处理，HttpClient在各个请求之间自动处理cookie保持
                List<CrawlLoginFilter> loginFilters = buildCrawlLoginFilters();
                Map<String, String> requestHeaders = Maps.newHashMap();
                requestHeaders.put("User-Agent", User_Agent);
                requestHeaders.put("Connection", "close");
                for (CrawlLoginFilter loginFilter : loginFilters) {
                    loginFilter.filter(url);
                }

                CloseableHttpResponse httpGetResponse = null;
                HttpEntity httpGetResponseEntity = null;
                try {
                    //首先对url进行GET请求处理
                    HttpGet httpGet = new HttpGet(url);
                    for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                        httpGet.addHeader(header.getKey(), header.getValue());
                    }
                    httpGetResponse = buildHttpClient().execute(httpGet);
                    statusCode = httpGetResponse.getStatusLine().getStatusCode();

                    //如果是301或302转向，则提取转向URL地址
                    if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
                        Header[] headers = httpGetResponse.getHeaders("Location");
                        if (headers.length > 0) {
                            redirectUrl = headers[0].getValue();
                        }
                    } else {
                        httpGetResponseEntity = httpGetResponse.getEntity();
                        result = responseEntityToString(url, httpGetResponseEntity);
                    }
                } catch (Exception e) {
                    statusCode = -1;
                    exception = Exceptions.getStackTraceAsString(e);
                } finally {
                    EntityUtils.consumeQuietly(httpGetResponseEntity);
                    IOUtils.closeQuietly(httpGetResponse);
                }

                //301/302转向，二次发起请求
                int redirectTimes = 0;
                while ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                        && redirectTimes < MAX_REDIRECT_TIMES) {
                    logger.info("Try GET redirect URL: {}", redirectUrl);
                    redirectTimes++;
                    try {
                        HttpGet httpGet = new HttpGet(redirectUrl);
                        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                            httpGet.addHeader(header.getKey(), header.getValue());
                        }
                        httpGetResponse = buildHttpClient().execute(httpGet);
                        statusCode = httpGetResponse.getStatusLine().getStatusCode();

                        //如果是302转向，则提取转向URL地址
                        if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                            Header[] headers = httpGetResponse.getHeaders("Location");
                            if (headers.length > 0) {
                                redirectUrl = headers[0].getValue();
                            }
                        } else {
                            httpGetResponseEntity = httpGetResponse.getEntity();
                            result = responseEntityToString(url, httpGetResponseEntity);
                        }
                    } catch (Exception e) {
                        statusCode = -1;
                        exception = Exceptions.getStackTraceAsString(e);
                    } finally {
                        EntityUtils.consumeQuietly(httpGetResponseEntity);
                        IOUtils.closeQuietly(httpGetResponse);
                    }
                }

                //GET不支持，尝试POST
                if (statusCode == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                    logger.info("Try to use POST as GET not allowed for URL: {}", statusCode, url);
                    CloseableHttpResponse httpPostResponse = null;
                    HttpEntity httpPostResponseEntity = null;
                    try {
                        HttpPost httpPost = new HttpPost(url);
                        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                            httpPost.addHeader(header.getKey(), header.getValue());
                        }
                        //POST请求一般是JSON形式的分页处理等
                        StringEntity entity = new StringEntity("{}", Default_Charset_UTF8);
                        entity.setContentEncoding(Default_Charset_UTF8);
                        entity.setContentType("application/json");
                        httpPost.setEntity(entity);
                        httpPostResponse = buildHttpClient().execute(httpPost);
                        statusCode = httpPostResponse.getStatusLine().getStatusCode();

                        httpPostResponseEntity = httpPostResponse.getEntity();
                        result = EntityUtils.toString(httpPostResponseEntity, Default_Charset_UTF8);
                    } catch (Exception e) {
                        statusCode = -1;
                        exception = Exceptions.getStackTraceAsString(e);
                    } finally {
                        EntityUtils.consumeQuietly(httpPostResponseEntity);
                        IOUtils.closeQuietly(httpPostResponse);
                    }
                }

                fetchUpdate.put("httpStatus", statusCode);
                fetchUpdate.put("httpResponse", result);

                if (statusCode != HttpStatus.SC_OK) {
                    logger.warn("HTTP ERROR StatusCode is {} for URL: {}", statusCode, url);
                    logger.trace("HTTP response for URL {} is:\n{}", url, result);

                    //页面Fetch处理异常,则重新注入尝试下次重新fetch和parse,超过阈值以后就不再对此url发起http请求
                    injectFetchFailureRetry(url, statusCode, result, exception);
                } else {
                    fetchUpdate.put("fetchFailureTimes", 0);
                    fetchUpdate.put("httpResponse", result);
                    fetchUpdate.put("fetchTime", new Date());
                    logger.info("HTTP Fetch is OK[{}] for URL: {}", statusCode, url);
                    fetchObjectCollection.update(urlQuery, new BasicDBObject("$set", fetchUpdate));
                }
            } else {
                logger.info("Skipped fetch [{}] as last fetched time: {}", url, fetchTime);
                statusCode = (Integer) item.get("httpStatus");
                result = (String) item.get("httpResponse");
                fetchObjectCollection.update(urlQuery, new BasicDBObject("$set", fetchUpdate));
            }

            //解析处理
            Date parseTime = (Date) item.get("parseTime");
            //如果没解析过或强制解析
            if (parseTime == null || crawlConfig.isForceReparse()) {
                //只对200状态响应进行解析处理
                if (statusCode == HttpStatus.SC_OK) {
                    logger.info("Parsing for URL: {}", url);
                    //获取所有有效的解析过滤器
                    List<CrawlParseFilter> parseFilters = buildCrawlParseFilters();

                    //取当前URL数据是否有指定的解析过滤器集合，如果有则按照指定过滤器集合处理，否则按照所有解析器处理
                    BasicDBList dbCrawlParseFilters = (BasicDBList) item.get("crawlParseFilters");
                    if (dbCrawlParseFilters != null && dbCrawlParseFilters.size() > 0) {
                        parseFilters = Lists.newArrayList();
                        for (Object dbItem : dbCrawlParseFilters) {
                            CrawlParseFilter crawlParseFilter = (CrawlParseFilter) ClassUtils.newInstance(dbItem.toString());
                            crawlParseFilter.setCrawlService(crawlService);
                            crawlParseFilter.setFilterPattern(Pattern.compile("^http.*"));
                            parseFilters.add(crawlParseFilter);
                        }
                    }

                    WebPage webPage = new WebPage();
                    webPage.setUrl(url);
                    webPage.setBizSiteName((String) item.get("bizSiteName"));
                    webPage.setBizId((String) item.get("bizId"));
                    webPage.setTitle((String) item.get("title"));

                    DBObject parsedDBObject = null;
                    try {
                        //逐个调用解析过滤器，如果返回了有效的解析对象则提取主要的分组和ID标识信息
                        for (CrawlParseFilter parseFilter : parseFilters) {
                            webPage.setPageText(result);
                            DBObject filterParsedDBObject = parseFilter.filter(url, webPage);
                            if (filterParsedDBObject != null) {
                                parsedDBObject = filterParsedDBObject;
                                webPage.setBizSiteName((String) parsedDBObject.get(CrawlParseFilter.SITE_NAME));
                                webPage.setBizId((String) parsedDBObject.get(CrawlParseFilter.ID));
                            }
                        }

                        //提取所有解析追加的outlink集合，注入下一轮循环处理
                        int outlinksSize = webPage.getOutlinks().size();
                        List<String> outlinks = Lists.newArrayList();
                        if (outlinksSize > 0) {
                            for (Outlink outlink : webPage.getOutlinks().values()) {
                                if (StringUtils.isBlank(outlink.getBizSiteName())) {
                                    outlink.setBizSiteName(webPage.getBizSiteName());
                                }
                                if (StringUtils.isBlank(outlink.getBizId())) {
                                    outlink.setBizId(webPage.getBizId());
                                }
                                outlinks.add(outlink.getUrl());
                                injectOutlink(outlink, crawlConfig, url);
                            }
                        } else {
                            //如果当前URL处理完后，既没有返回解析业务数据，也没有输出outlink，则warn警告此无意义处理url
                            if (parsedDBObject == null) {
                                logger.warn("NO Output after all filter for URL: {}", url);
                            }
                        }

                        //增量更新相关解析属性
                        DBObject update = new BasicDBObject();
                        update.put("bizSiteName", webPage.getBizSiteName());
                        update.put("bizId", webPage.getBizId());
                        update.put("title", webPage.getTitle());
                        update.put("outlinks", outlinks);
                        update.put("parseTime", new Date());
                        update.put("parseFailureTimes", 0);
                        update.put("parseFailureException", null);
                        fetchObjectCollection.update(urlQuery, new BasicDBObject("$set", update));
                    } catch (Exception e) {
                        // 解析处理异常, 再次inject尝试重新fetch和parse
                        logger.error("Parse exception for: " + url, e);
                        injectParseFailureRetry(webPage, Exceptions.getStackTraceAsString(e));
                    }
                } else {
                    logger.debug("Skipped parse [{}] as HTTP status code is  {}", url, statusCode);
                }
            } else {
                //如果无需解析处理，则直接把之前的outlink集合注入下一轮循环处理
                logger.debug("Skipped parse [{}] as last parsed time: {}", url, parseTime);
                BasicDBList dbList = (BasicDBList) item.get("outlinks");
                if (dbList != null) {
                    for (Object dbItem : dbList) {
                        Outlink outlink = new Outlink();
                        outlink.setUrl(dbItem.toString());
                        injectOutlink(outlink, crawlConfig, url);
                    }
                }
            }

            //把异常记录搬迁到一个指定集合便于快速查询
            DBCollection failureObjectCollection = buildFailureObjectCollection();
            DBObject fetchItem = fetchObjectCollection.findOne(urlQuery,
                    new BasicDBObject().append("parseFailureTimes", 1).append("fetchFailureTimes", 1));
            Integer fetchFailureTimes = (Integer) fetchItem.get("fetchFailureTimes");
            Integer parseFailureTimes = (Integer) fetchItem.get("parseFailureTimes");
            DBObject failureItem = failureObjectCollection.findOne(urlQuery, urlQuery);
            //假如失败都为0说明处理成功，移除
            if ((fetchFailureTimes == null || fetchFailureTimes == 0 || statusCode == HttpStatus.SC_OK)
                    && (parseFailureTimes == null || parseFailureTimes == 0)) {
                if (failureItem != null) {
                    failureObjectCollection.remove(failureItem);
                }
            } else {
                DBObject upset = fetchObjectCollection.findOne(urlQuery);
                upset.put("id", url);
                failureObjectCollection.update(urlQuery, upset, true, false);
            }

            //累加计数器，并打印一些重要的跟踪信息
            pages.incrementAndGet();
            if (logger.isInfoEnabled()) {
                long elapsed = (System.currentTimeMillis() - crawlStartTime.getTime()) / 1000;
                float avgPagesSec = (float) pages.get() / elapsed;
                logger.info("Total fetched and parsed " + pages.get() + " pages, " + elapsed + " seconds, avg " + avgPagesSec + " pages/s ,thread ["
                        + executor.getActiveCount() + "/" + executor.getCorePoolSize() + "]");
            }
        }
    }

    /**
     * 从MongoDB中查询所有作为种子注入的URL列表
     * @return
     */
    public List<DBObject> findSeedURLs() {
        List<DBObject> seedURLs = Lists.newArrayList();
        DBCollection dbColl = buildFetchObjectCollection();
        BasicDBObject query = new BasicDBObject("injectSeed", Boolean.TRUE);
        BasicDBObject keys = new BasicDBObject();
        keys.put(CrawlParseFilter.URL, 1);
        keys.put("injectTime", 1);
        DBCursor cur = dbColl.find(query, keys).sort(new BasicDBObject("injectTime", 0));
        while (cur.hasNext()) {
            DBObject item = cur.next();
            seedURLs.add(item);
        }
        return seedURLs;
    }

    public void convertLongToDate(String name) {
        DBCollection coll = buildFetchObjectCollection();
        int cnt = 1;
        DBCursor cur = coll.find(new BasicDBObject(name, new BasicDBObject("$type", 16)), new BasicDBObject(CrawlParseFilter.URL, 1).append(name, 1));
        int total = cur.count();
        while (cur.hasNext()) {
            DBObject item = cur.next();
            String url = (String) item.get(CrawlParseFilter.URL);
            DBObject update = new BasicDBObject();
            Integer time = (Integer) item.get(name);
            if (time < 0) {
                time = 0;
            }
            update.put(name, new Date(time));
            logger.debug("{}/{}. Convert type for: {}", cnt++, total, url);
            coll.update(new BasicDBObject(CrawlParseFilter.URL, url), new BasicDBObject("$set", update));
        }
    }

    public List<String> getAllSiteNameList() {
        DBCollection coll = buildParseObjectCollection();
        List<String> bizSiteNameList = coll.distinct("站点分组");
        return bizSiteNameList;
    }

    public OperationResult generateThreadStart(String bizSiteName) {
        new Thread(new GenerateThread(this, bizSiteName)).start();
        return OperationResult.buildSuccessResult("正在生成文件，请稍后刷新");
    }

    private class GenerateThread implements Runnable {
        private CrawlService crawlService;
        private String bizSiteName;

        public GenerateThread(CrawlService crawlService, String bizSiteName) {
            this.crawlService = crawlService;
            this.bizSiteName = bizSiteName;
        }

        @Override
        public void run() {
            crawlService.generateFile(bizSiteName);
        }
    }

    public String generateFile(String bizSiteName) {
        try {
            DBCollection coll = buildParseObjectCollection();
            List<String> bizSiteNameList = coll.distinct("站点分组");

            if (CollectionUtils.isEmpty(bizSiteNameList)) {
                return null;
            }

            Set<String> bizSiteNameSet = Sets.newHashSet();
            bizSiteNameSet.addAll(bizSiteNameList);

            BasicDBObject query = null;
            if (StringUtils.isNotBlank(bizSiteName)) {
                bizSiteName = bizSiteName.trim();
                if (!bizSiteNameSet.contains(bizSiteName)) {
                    return null;
                } else {
                    query = new BasicDBObject("站点分组", bizSiteName);
                    DBCursor dBCursor = coll.find(query);
                    return exportXls(dBCursor, bizSiteName);
                }
            } else {
                List<String> files = Lists.newArrayList();
                for (String siteName : bizSiteNameList) {
                    query = new BasicDBObject("站点分组", siteName);
                    DBCursor dBCursor = coll.find(query);
                    files.add(exportXls(dBCursor, siteName));
                }
                return StringUtils.join(files, ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String exportXls(DBCursor dBCursor, String bizSiteName) throws Exception {
        if (dBCursor.count() <= 0) {
            dBCursor.close();
            return null;
        }

        //复制DBCursor
        DBCursor dBCursorClone = dBCursor.copy();

        //计算表头
        Set<String> titleSet = Sets.newHashSet();
        while (dBCursor.hasNext()) {
            BasicDBObject dBObject = (BasicDBObject) dBCursor.next();
            Set<String> keySet = dBObject.keySet();
            titleSet.addAll(keySet);
        }
        dBCursor.close();

        if (CollectionUtils.isEmpty(titleSet)) {
            return null;
        }
        List<String> titleList = Lists.newArrayList(titleSet);

        HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象   
        HSSFSheet sheet = wb.createSheet();//创建Excel工作表对象     

        HSSFRow titleRow = sheet.createRow(0);//创建表头
        for (int i = 0; i < titleList.size(); i++) {
            HSSFCell hssfCell = titleRow.createCell(i);
            hssfCell.setCellValue(titleList.get(i));
        }

        int rowIndex = 1;
        HSSFRow hssfRow = null;
        while (dBCursorClone.hasNext()) {
            BasicDBObject dBObject = (BasicDBObject) dBCursorClone.next();
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < titleList.size(); i++) {
                HSSFCell hssfCell = hssfRow.createCell(i);
                String cell = dBObject.getString(titleList.get(i));
                if (StringUtils.isEmpty(cell)) {
                    cell = "";
                }
                hssfCell.setCellValue(cell);
            }
            rowIndex++;
        }
        dBCursorClone.close();
        //
        String crawlExportDir = FileUtils.getTempDirectoryPath() + File.separator + "crawl" + File.separator + "export";
        File directory = new File(crawlExportDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String exportFilePath = crawlExportDir + File.separator + bizSiteName + ".xls";
        logger.debug("Save {} crawl Excel export data to file: {}", bizSiteName, exportFilePath);
        FileOutputStream fileOutputStream = new FileOutputStream(exportFilePath);

        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();

        return exportFilePath;
    }
}
