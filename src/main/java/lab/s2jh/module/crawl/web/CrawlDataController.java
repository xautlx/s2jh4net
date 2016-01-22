package lab.s2jh.module.crawl.web;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.crawl.CrawlParseFilter;
import lab.s2jh.core.dao.mongo.MongoDao;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.web.json.JsonViews;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.crawl.service.CrawlService;
import lab.s2jh.module.crawl.vo.CrawlConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping(value = "/admin/crawl/crawl-data")
public class CrawlDataController {

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private MongoDao mongoDao;

    @Value("${crawl_appender_netty_port}")
    private String crawlAppenderNettyPort;

    @MenuData("爬虫数据采集:爬虫数据管理")
    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/crawl/crawlData-index";
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/logger", method = RequestMethod.GET)
    public String fetchLogger(Model model, HttpServletRequest request) {
        model.addAttribute("crawlAppenderNettyContextURL", request.getScheme() + "://" + request.getServerName() + ":" + crawlAppenderNettyPort);
        return "admin/crawl/crawlData-crawlerLogger";
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/crawler", method = RequestMethod.GET)
    public String crawlerIndex(Model model) {
        model.addAttribute("crawlConfig", new CrawlConfig());
        List<CrawlParseFilter> crawlParseFilters = crawlService.buildCrawlParseFilters();
        model.addAttribute("crawlParseFilters", crawlParseFilters);
        return "admin/crawl/crawlData-crawlerIndex";
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/crawler/startup", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult startup(@RequestParam("urls") String urls, @ModelAttribute("crawlConfig") CrawlConfig crawlConfig, Model model) {
        crawlService.startup(crawlConfig, StringUtils.split(urls, ";\n"));
        return OperationResult.buildSuccessResult("爬虫数据采集任务已提交并开始处理");
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/crawler/shutdown", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult shutdown(Model model) {
        crawlService.shutdown();
        return OperationResult.buildSuccessResult("爬虫数据采集任务已提交终止");
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/failure", method = RequestMethod.GET)
    public String failureIndex(Model model) {
        return "admin/crawl/crawlData-failureIndex";
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/failure-data-list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<DBObject> findByPage(HttpServletRequest request) {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(Persistable.class, request);
        return mongoDao.findPage(CrawlService.Crawl_Failure_Data_Collection, groupFilter, pageable,
                new BasicDBObject("httpResponse", 0).append("fetchFailureException", 0).append("fetchParseException", 0));
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/generation-file", method = RequestMethod.GET)
    public String generationFile(Model model) {
        List<Map<String, String>> fileListMap = Lists.newArrayList();
        String crawlExportDir = FileUtils.getTempDirectoryPath() + File.separator + "crawl" + File.separator + "export";
        File directory = new File(crawlExportDir);
        if (directory != null && directory.exists()) {
            List<File> fileList = (List<File>) FileUtils.listFiles(directory, new String[] { "xls" }, true);
            for (File file : fileList) {
                Map<String, String> map = Maps.newHashMap();
                map.put("fileName", file.getName());
                map.put("fileSize", String.format("%.2f", file.length() * 1.0 / 1024 / 1024) + "M");
                map.put("lastModified", DateUtils.formatTime(new Date(file.lastModified())));
                fileListMap.add(map);
            }
        }
        model.addAttribute("fileList", fileListMap);
        model.addAttribute("allSiteNameList", crawlService.getAllSiteNameList());
        return "admin/crawl/crawlData-generationFile";
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/generation-file/generate", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult generate(@RequestParam(value = "bizSiteName", required = false) String bizSiteName, Model model) {
        return crawlService.generateThreadStart(bizSiteName);
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/generation-file/download", method = RequestMethod.GET)
    @ResponseBody
    public void download(@RequestParam(value = "fileName") String fileName, Model model, HttpServletResponse response) {
        String crawlExportDir = FileUtils.getTempDirectoryPath() + File.separator + "crawl" + File.separator + "export";
        File file = new File(crawlExportDir + File.separator + fileName);
        ServletUtils.renderFileDownload(response, file);
    }

    @RequiresPermissions("爬虫数据采集:爬虫数据管理")
    @RequestMapping(value = "/failure-detail", method = RequestMethod.GET)
    public String failureDetail(Model model, HttpServletRequest request) {
        DBObject entity = mongoDao.getDB().getCollection(CrawlService.Crawl_Failure_Data_Collection)
                .findOne(new BasicDBObject("id", request.getParameter("id")));
        model.addAttribute("datasMap", entity.toMap());
        return "admin/crawl/crawlData-failureDetail";
    }
}
