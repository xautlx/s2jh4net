/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.support.web;

import ch.qos.logback.classic.Level;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cache.AppCacheService;
import com.entdiy.core.exception.WebException;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.Exceptions;
import com.entdiy.core.util.ExtStringUtils;
import com.entdiy.core.util.FileUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.captcha.CaptchaUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.support.service.SmsService;
import com.entdiy.support.service.SmsService.SmsMessageTypeEnum;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.service.AttachmentFileService;
import com.entdiy.sys.service.SmsVerifyCodeService;
import com.google.common.collect.Maps;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class UtilController {

    private final static Logger logger = LoggerFactory.getLogger(UtilController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SmsVerifyCodeService smsVerifyCodeService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Autowired(required = false)
    private SmsService smsService;

    @Autowired
    private List<AppCacheService> appCacheServices;

    @RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
    public String unauthorizedUrl() {
        return "error/403";
    }

    /**
     * <h3>APP接口: 发送短信验证码。</h3>
     * <p>从接口会向所有手机号发送短信验证码，但是可能在极端情况通过全局系统参数关闭向开放手机发送短信功能。</p>
     * <p>此接口主要适用于开放式的注册验证码发送功能，如果调用端功能能明确是向已注册用户发送短信，如找回密码功能，则请用/user-sms-code接口</p>
     * <p>
     * <p>
     * <p>
     * 业务输入参数列表：
     * <ul>
     * <li><b>mobile</b> 手机号</li>
     * </ul>
     * </p>
     *
     * @return {@link OperationResult} 通用标准结构
     */
    @RequestMapping(value = "/pub/send-sms-code/{mobile}", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult sendSmsCode(@PathVariable("mobile") String mobile, HttpServletRequest request) {
        //二次校验验证码避免绕过表单校验的恶意请求
        CaptchaUtils.assetValidateCaptchaCode(request, "captcha");

        String code = smsVerifyCodeService.generateSmsCode(request, mobile, false);
        String msg = "您的操作验证码为：" + code + "。请勿向任何人提供您收到的短信验证码。如非本人操作，请忽略本信息。";
        String errorMessage = smsService.sendSMS(msg, mobile, SmsMessageTypeEnum.VerifyCode);
        if (StringUtils.isBlank(errorMessage)) {
            OperationResult result = OperationResult.buildSuccessResult();
            //如果是开发模式直接把短信内容返回给页面显示方便开发调试
            if (AppContextHolder.isDevMode()) {
                result.setMessage("开发模式信息：" + msg);
            }
            return result;
        } else {
            return OperationResult.buildFailureResult(errorMessage);
        }
    }

    @MenuData("配置管理:系统管理:辅助管理")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/admin/util", method = RequestMethod.GET)
    public String index() {
        return "admin/util/util-index";
    }

    @MetaData(value = "刷新数据缓存")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/admin/util/cache-clear", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult dataEvictCache() {
        logger.info("Spring cacheEvict...");
        for (AppCacheService appCacheService : appCacheServices) {
            appCacheService.cacheEvictAll();
        }
        return OperationResult.buildSuccessResult("数据缓存刷新操作成功");
    }

    @MetaData(value = "动态更新Logger日志级别")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/admin/util/logger-update", method = RequestMethod.POST)
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

    @RequestMapping(value = "/admin/util/validate/unique", method = RequestMethod.GET)
    @ResponseBody
    public boolean formValidationUnique(HttpServletRequest request, Model model, @RequestParam("clazz") String clazz) {
        String element = request.getParameter("element");
        Assert.notNull(element, "element parameter is required");

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
    @RequestMapping(value = "/admin/util/grid/export", method = RequestMethod.POST)
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

            HSSFWorkbook wb = new HSSFWorkbook();// 创建Excel工作簿对象
            HSSFSheet sheet = wb.createSheet(filename);// 创建Excel工作表对象
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

    @RequestMapping(value = "/admin/util/load-balance-test", method = RequestMethod.GET)
    public String loadBalanceTest() {
        return "admin/util/load-balance-test";
    }

    @MetaData(value = "系统时间篡改更新")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/admin/util/systime/setup", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult systimeSetup(@RequestParam(value = "time", required = true) LocalDateTime dateTime) {
        Validation.notDemoMode();
        DateUtils.setCurrentDateTime(dateTime);

        // 为了避免遗忘执行手工恢复操作，在“临时调整系统时间”操作后，默认在N分钟后强制恢复为当前系统时间。
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DateUtils.setCurrentDateTime(null);
                logger.info("Processed DateUtils.currentDateTime() reset to new Date()");
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(runnable, 5, TimeUnit.MINUTES);

        return OperationResult.buildSuccessResult("系统时间已临时调整为：" + dateTime);
    }

    @MetaData(value = "系统时间篡改恢复")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_SUPER_USER)
    @RequestMapping(value = "/admin/util/systime/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult systimeReset() {
        DateUtils.setCurrentDateTime(null);
        return OperationResult.buildSuccessResult("系统时间临时调整已恢复为当前系统时间：" + DateUtils.currentDateTime());
    }


    @RequestMapping(value = "/pub/file-upload/kind-editor", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> imageUpload(HttpServletRequest request, @RequestParam("imgFile") CommonsMultipartFile fileUpload) {
        Map<String, Object> retMap = Maps.newHashMap();
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                String fileName = fileUpload.getOriginalFilename();
                long fileLength = fileUpload.getSize();

                //写入磁盘文件
                FileUtils.FileInfo fileInfo = FileUtils.writeFile(fileUpload.getInputStream(), FileUtils.SUB_DIR_FILES, fileName, fileLength);

                //创建附件记录
                AttachmentFile attachmentFile = new AttachmentFile();
                attachmentFile.setFileRealName(fileName);
                attachmentFile.setFileLength(fileLength);
                attachmentFile.setFileContentType(fileUpload.getContentType());
                attachmentFile.setRelativePath(fileInfo.getRelativePath());
                attachmentFile.setAbsolutePath(fileInfo.getAbsolutePath());
                attachmentFileService.save(attachmentFile);

                //以下两个属性用于kindeditor显示之用
                retMap.put("error", 0);
                retMap.put("url", request.getContextPath() + attachmentFile.getAccessUrl());

                //业务使用属性
                retMap.put("id", attachmentFile.getId());
                retMap.put("relativePath", attachmentFile.getRelativePath());
                retMap.put("fileRealName", attachmentFile.getFileRealName());
                retMap.put("fileLength", attachmentFile.getFileLength());
                return retMap;
            }
        } catch (IOException e) {
            throw new WebException("Upload file error", e);
        }
        retMap.put("error", 1);
        retMap.put("message", "图片处理失败");
        return retMap;
    }

    @RequestMapping(value = "/pub/file/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void fileDownload(@PathVariable("id") String id, HttpServletResponse response) {
        attachmentFileService.findOne(id).ifPresent(attachmentFile -> {
            try {
                String absolutePath = attachmentFile.getAbsolutePath();
                if (absolutePath.startsWith("http://") || absolutePath.startsWith("https://")) {
                    response.sendRedirect(absolutePath);
                } else {
                    String fileName = URLEncoder.encode(attachmentFile.getFileRealName(), "UTF-8");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                    response.addHeader("Content-Length", "" + attachmentFile.getFileLength());
                    response.setContentType(attachmentFile.getFileContentType());
                    InputStream in = new FileInputStream(new File(absolutePath));
                    OutputStream out = response.getOutputStream();
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            } catch (Exception e) {
                logger.error("File download error", e);
            }
        });
    }
}
