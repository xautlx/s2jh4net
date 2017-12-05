package com.entdiy.support.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import com.entdiy.core.exception.ServiceException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class FreemarkerService extends Configuration {

    private final static Logger logger = LoggerFactory.getLogger(FreemarkerService.class);

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    public FreemarkerService() {
        this.setDefaultEncoding("UTF-8");
        this.setTemplateLoader(stringTemplateLoader);
    }

    public String processTemplate(String templateName, long version, String templateContents, Map<String, Object> dataMap) {
        Assert.notNull(templateName);
        Assert.notNull(version);
        if (StringUtils.isBlank(templateContents)) {
            return null;
        }
        Object templateSource = stringTemplateLoader.findTemplateSource(templateName);
        if (templateSource == null) {
            logger.debug("Init freemarker template: {}", templateName);
            stringTemplateLoader.putTemplate(templateName, templateContents, version);
        } else {
            long ver = stringTemplateLoader.getLastModified(templateSource);
            if (version > ver) {
                logger.debug("Update freemarker template: {}", templateName);
                stringTemplateLoader.putTemplate(templateName, templateContents, version);
            }
        }
        return processTemplateByName(templateName, dataMap);
    }

    private String processTemplateByName(String templateName, Map<String, Object> dataMap) {
        StringWriter strWriter = new StringWriter();
        try {
            this.getTemplate(templateName).process(dataMap, strWriter);
            strWriter.flush();
        } catch (TemplateException e) {
            throw new ServiceException("error.freemarker.template.process", e);
        } catch (IOException e) {
            throw new ServiceException("error.freemarker.template.process", e);
        }
        return strWriter.toString();
    }

    public String processTemplateByContents(String templateContents, Map<String, Object> dataMap) {
        String templateName = "_" + templateContents.hashCode();
        return processTemplate(templateName, 0, templateContents, dataMap);
    }

    public String processTemplateByFileName(String templateFileName, Map<String, Object> dataMap) {
        String templateDir = FileUtils.getTempDirectoryPath() + File.separator + "template" + File.separator + "freemarker";
        File targetTemplateFile = new File(templateDir + File.separator + templateFileName + ".ftl");
        if (!targetTemplateFile.exists()) {
            try {
                //从classpath加载文件处理写入临时文件
                InputStream source = this.getClass().getResourceAsStream("/template/freemarker/" + templateFileName + ".ftl");
                FileUtils.copyInputStreamToFile(source, targetTemplateFile);
            } catch (IOException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
        logger.debug("Processing freemarker template file: {}", targetTemplateFile.getAbsolutePath());
        long fileVersion = targetTemplateFile.lastModified();
        Object templateSource = stringTemplateLoader.findTemplateSource(templateFileName);
        long templateVersion = 0;
        if (templateSource != null) {
            templateVersion = stringTemplateLoader.getLastModified(templateSource);
        }
        if (fileVersion > templateVersion) {
            try {
                String contents = FileUtils.readFileToString(targetTemplateFile);
                return processTemplate(templateFileName, fileVersion, contents, dataMap);
            } catch (IOException e) {
                throw new ServiceException("error.freemarker.template.process", e);
            }
        } else {
            return processTemplateByName(templateFileName, dataMap);
        }
    }
}
