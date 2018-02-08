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
package com.entdiy.support.service;

import com.entdiy.core.exception.ServiceException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

@Service
public class FreemarkerService extends Configuration {

    private final static Logger logger = LoggerFactory.getLogger(FreemarkerService.class);

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    public FreemarkerService() {
        this.setDefaultEncoding("UTF-8");
        this.setTemplateLoader(stringTemplateLoader);
    }

    public String processTemplate(String templateName, long version, String templateContents, Map<String, Object> dataMap) {
        Assert.notNull(templateName, "templateName is required");
        Assert.notNull(version, "version is required");
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
