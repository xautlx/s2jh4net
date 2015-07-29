package lab.s2jh.support.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.mq.BrokeredMessageListener;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.Validation;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.core.util.ExtStringUtils;
import lab.s2jh.core.web.filter.WebAppContextInitFilter;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Level;

@Controller
@RequestMapping("/admin/util")
public class UtilController {

    private final static Logger logger = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private CacheManager cacheManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired(required = false)
    private BrokeredMessageListener brokeredMessageListener;

    @MenuData("配置管理:系统管理:辅助管理")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/util/util-index";
    }

    @MetaData(value = "刷新数据缓存")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/cache-clear", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult dataEvictCache() {
        WebAppContextInitFilter.reset();
        if (cacheManager != null) {
            logger.info("Clearing EhCache cacheManager: {}", cacheManager.getName());
            String[] cacheNames = cacheManager.getCacheNames();
            for (String cacheName : cacheNames) {
                //对于Apache Shiro缓存忽略
                if (cacheName.indexOf(".authorizationCache") > -1) {
                    continue;
                }
                logger.debug(" - clearing cache： {}", cacheName);
                Ehcache cache = cacheManager.getEhcache(cacheName);
                cache.removeAll();
            }
            return OperationResult.buildSuccessResult("EhCache数据缓存刷新操作成功");
        }
        return OperationResult.buildFailureResult("未知缓存管理器");
    }

    @MetaData(value = "动态更新Logger日志级别")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/logger-update", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult loggerLevelUpdate(@RequestParam(value = "loggerName", required = false) String loggerName,
            @RequestParam("loggerLevel") String loggerLevel) {
        Validation.notDemoMode();
        if (StringUtils.isBlank(loggerName)) {
            loggerName = Logger.ROOT_LOGGER_NAME;
        }
        Logger logger = LoggerFactory.getLogger(loggerName);
        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
        if (StringUtils.isNotBlank(loggerLevel)) {
            logbackLogger.setLevel(Level.toLevel(loggerLevel));
        }
        logger.info("Update logger {} to level {}", loggerName, loggerLevel);
        return OperationResult.buildSuccessResult("动态更新Logger日志级别操作成功");
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> formValidation(Model model, @RequestParam("clazz") String clazz) {
        return ServletUtils.buildValidateRules(clazz);
    }

    @RequestMapping(value = "/validate/unique", method = RequestMethod.GET)
    @ResponseBody
    public boolean formValidationUnique(HttpServletRequest request, Model model, @RequestParam("clazz") String clazz) {
        String element = request.getParameter("element");
        Assert.notNull(element);

        String value = request.getParameter(element);
        if (!ExtStringUtils.hasChinese(value)) {
            value = ExtStringUtils.encodeUTF8(value);
        }

        Class<?> entityClass = ClassUtils.forName(clazz);
        String jql = "select id from " + entityClass.getName() + " where " + element + "=:value ";
        Query query = null;

        // 处理额外补充参数，有些数据是通过两个字段共同决定唯一性，可以通过additional参数补充提供
        String additionalName = request.getParameter("additional");
        if (StringUtils.isNotBlank(additionalName)) {
            String additionalValue = request.getParameter(additionalName);
            if (!ExtStringUtils.hasChinese(additionalValue)) {
                additionalValue = ExtStringUtils.encodeUTF8(additionalValue);
            }
            jql = jql + additionalName + "=:additionalValue ";
            query = entityManager.createQuery(jql);
            query.setParameter("value", value);
            query.setParameter("additionalValue", additionalValue);
        } else {
            query = entityManager.createQuery(jql);
            query.setParameter("value", value);
        }

        List<?> entities = query.getResultList();
        if (entities == null || entities.size() == 0) {// 未查到重复数据
            return true;
        } else {
            if (entities.size() == 1) {// 查询到一条重复数据
                String id = request.getParameter("id");
                if (StringUtils.isNotBlank(id)) {
                    String entityId = ((Long) entities.get(0)).toString();
                    logger.debug("Check Unique Entity ID = {}", entityId);
                    if (id.equals(entityId)) {// 查询到数据是当前更新数据，不算已存在
                        return true;
                    } else {// 查询到数据不是当前更新数据，算已存在
                        return false;
                    }
                } else {// 没有提供Sid主键，说明是创建记录，则算已存在
                    return false;
                }
            } else {// 查询到多余一条重复数据，说明数据库数据本身有问题
                throw new WebException("error.check.unique.duplicate: " + element + "=" + value);
            }
        }
    }

    /**
     * 基于jqGrid页面数据实现一个通用的导出Excel功能
     * 注意：此功能只处理页面已有数据，不包括分页支持；如果需要导出当前所有查询出来的数据需要另行实现
     */
    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/grid/export", method = RequestMethod.POST)
    @ResponseBody
    public void gridExport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = request.getParameter("fileName");
            filename = new String(filename.getBytes("GBK"), "ISO-8859-1");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            String exportDatas = request.getParameter("exportDatas");
            OutputStream os = response.getOutputStream();

            HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象   
            HSSFSheet sheet = wb.createSheet(filename);//创建Excel工作表对象     
            String[] rows = exportDatas.split("\n");
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i];
                if (StringUtils.isNotBlank(row)) {
                    logger.trace("Row {}: {}", i, row);
                    // 创建Excel的sheet的一行
                    HSSFRow hssfRow = sheet.createRow(i);
                    String[] cells = row.split("\t");
                    for (int j = 0; j < cells.length; j++) {
                        String cell = cells[j];
                        // 创建一个Excel的单元格
                        HSSFCell hssfCell = hssfRow.createCell(j);
                        hssfCell.setCellValue(cell);
                    }
                }
            }
            wb.write(os);
            IOUtils.closeQuietly(os);
        } catch (UnsupportedEncodingException e) {
            Exceptions.unchecked(e);
        } catch (IOException e) {
            Exceptions.unchecked(e);
        }
    }

    @RequestMapping(value = "/load-balance-test", method = RequestMethod.GET)
    public String loadBalanceTest() {
        return "admin/util/load-balance-test";
    }

    @MetaData(value = "系统时间篡改更新")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/systime/setup", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult systimeSetup(@RequestParam(value = "time", required = true) String time) {
        DateUtils.setCurrentDate(DateUtils.parseMultiFormatDate(time));

        //为了避免遗忘执行手工恢复操作，在“临时调整系统时间”操作后，默认在N分钟后强制恢复为当前系统时间。
        Runnable runnable = new Runnable() {
            public void run() {
                DateUtils.setCurrentDate(null);
                logger.info("Processed DateUtils.currentDate() reset to new Date()");
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(runnable, 5, TimeUnit.MINUTES);

        return OperationResult.buildSuccessResult("系统时间已临时调整为：" + DateUtils.formatTime(DateUtils.currentDate()));
    }

    @MetaData(value = "系统时间篡改恢复")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/systime/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult systimeReset() {
        DateUtils.setCurrentDate(null);
        return OperationResult.buildSuccessResult("系统时间临时调整已恢复为当前系统时间：" + DateUtils.formatTime(DateUtils.currentDate()));
    }

    @MetaData(value = "消息服务监听器状态切换")
    @RequiresRoles(AuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/brokered-message/listener-state", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult brokeredMessageState(@RequestParam(value = "state", required = true) String state) {
        Validation.isTrue(brokeredMessageListener != null, "BrokeredMessageListener undefined");
        if ("startup".equals(state)) {
            brokeredMessageListener.startup();
            return OperationResult.buildSuccessResult("消息服务监听器已启动");
        } else if ("shutdown".equals(state)) {
            brokeredMessageListener.shutdown();
            return OperationResult.buildSuccessResult("消息服务监听器已关闭");
        }
        return OperationResult.buildFailureResult("未知状态参数");
    }
}
