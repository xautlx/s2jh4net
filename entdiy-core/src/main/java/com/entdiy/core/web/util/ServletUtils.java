/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.web.util;

import ch.qos.logback.classic.ClassicConstants;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.Encodes;
import com.entdiy.core.web.filter.WebAppContextInitFilter;
import com.entdiy.core.web.json.DateJsonSerializer;
import com.entdiy.core.web.json.DateTimeJsonSerializer;
import com.entdiy.core.web.json.ShortDateTimeJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ClassUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;

public class ServletUtils {

    private final static Logger logger = LoggerFactory.getLogger(ServletUtils.class);

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    // 移动设备正则匹配：手机端、平板
    static java.util.regex.Pattern phonePat = java.util.regex.Pattern.compile(phoneReg, java.util.regex.Pattern.CASE_INSENSITIVE);
    static java.util.regex.Pattern tablePat = java.util.regex.Pattern.compile(tableReg, java.util.regex.Pattern.CASE_INSENSITIVE);

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * <p>
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> buildParameters(ServletRequest request) {
        Enumeration<?> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String prefix = "search_";
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((values == null) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    String val = values[0];
                    if (StringUtils.isNotBlank(val)) {
                        params.put(unprefixed, val);
                    }
                }
            }
        }
        return params;
    }

    /**
     * 基于文件对象渲染文件下载响应
     *
     * @param response
     * @param file
     */
    public static void renderFileDownload(HttpServletResponse response, File file) {
        OutputStream output = null;
        try {
            // 中文文件名支持
            String encodedfileName = new String(file.getName().getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");

            output = response.getOutputStream();
            FileUtils.copyFile(file, output);
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static Map<String, Map<String, Object>> entityValidationRulesMap = Maps.newHashMap();

    /**
     * 基于构建的哈希标识计算获取校验规则
     *
     * @return
     */
    public static Map<String, Object> buildValidateRules(String entityClazz) {
        Map<String, Object> nameRules = entityValidationRulesMap.get(entityClazz);
        try {
            // 开发模式则每次计算以便修改注解后及时生效
            if (nameRules == null || GlobalConfigService.isDevMode()) {
                nameRules = Maps.newHashMap();
                entityValidationRulesMap.put(entityClazz, nameRules);
                Class<?> clazz = ClassUtils.forName(entityClazz);

                Assert.notNull(clazz, "验证缓存数据错误");

                MetaData clazzMetaData = clazz.getAnnotation(MetaData.class);
                Map<String, Object> data = Maps.newHashMap();
                data.put("entityName", clazzMetaData.value());
                nameRules.put("_classMeta", data);

                Set<Field> fields = Sets.newHashSet(clazz.getDeclaredFields());
                clazz = clazz.getSuperclass();
                while (!clazz.equals(Object.class)) {
                    fields.addAll(Sets.newHashSet(clazz.getDeclaredFields()));
                    clazz = clazz.getSuperclass();
                }

                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers()) || !Modifier.isPrivate(field.getModifiers())
                            || Collection.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    String name = field.getName();
                    if ("id".equals(name) || "version".equals(name)) {
                        continue;
                    }
                    Map<String, Object> rules = Maps.newHashMap();

                    // TODO 优化嵌套属性处理
                    // 如果是实体对象类型，一般表单元素name都定义为entity.id，因此额外追加对应id属性校验规则
                    // if
                    // (PersistableEntity.class.isAssignableFrom(field.getType()))
                    // {
                    // nameRules.put(name + ".id", rules);
                    // }

                    MetaData metaData = field.getAnnotation(MetaData.class);
                    if (metaData != null) {
                        String tooltips = metaData.tooltips();
                        if (StringUtils.isNotBlank(tooltips)) {
                            rules.put("tooltips", tooltips);
                        }
                    }

                    Class<?> retType = field.getType();
                    Column column = field.getAnnotation(Column.class);

                    if (column != null) {
                        if (retType != Boolean.class && column.nullable() == false) {
                            rules.put("required", true);
                        }
                        if (column.unique() == true) {
                            rules.put("unique", true);
                        }
                        if (column.updatable() == false) {
                            rules.put("readonly", true);
                        }
                        if (column.length() > 0 && retType == String.class && field.getAnnotation(Lob.class) == null) {
                            rules.put("maxlength", column.length());
                        }
                    }

                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    if (joinColumn != null) {
                        if (joinColumn.nullable() == false) {
                            rules.put("required", true);
                        }
                    }

                    if (retType == Date.class) {
                        Temporal temporal = field.getAnnotation(Temporal.class);
                        if (temporal != null && temporal.value().equals(TemporalType.TIMESTAMP)) {
                            rules.put("timestamp", true);
                        } else {
                            rules.put("date", true);
                        }

                        DateTimeFormat dateTimeFormat = field.getAnnotation(DateTimeFormat.class);
                        if (dateTimeFormat != null) {
                            if (DateUtils.DEFAULT_DATE_FORMAT.equals(dateTimeFormat.pattern())) {
                                rules.put("date", true);
                            } else if (DateUtils.DEFAULT_TIME_FORMAT.equals(dateTimeFormat.pattern())) {
                                rules.put("timestamp", true);
                            } else if (DateUtils.SHORT_TIME_FORMAT.equals(dateTimeFormat.pattern())) {
                                rules.put("shortTimestamp", true);
                            } else {
                                rules.put("date", true);
                            }
                        }

                        JsonSerialize jsonSerialize = field.getAnnotation(JsonSerialize.class);
                        if (jsonSerialize != null) {
                            if (DateJsonSerializer.class == jsonSerialize.using()) {
                                rules.put("date", true);
                            } else if (DateTimeJsonSerializer.class == jsonSerialize.using()) {
                                rules.put("timestamp", true);
                                rules.remove("date");
                            } else if (ShortDateTimeJsonSerializer.class == jsonSerialize.using()) {
                                rules.put("shortTimestamp", true);
                                rules.remove("date");
                            }
                        }
                    } else if (retType == BigDecimal.class) {
                        rules.put("number", true);
                    } else if (retType == Integer.class || retType == Long.class) {
                        rules.put("digits", true);
                    }

                    Size size = field.getAnnotation(Size.class);
                    if (size != null) {
                        if (size.min() > 0) {
                            rules.put("minlength", size.min());
                        }
                        if (size.max() < Integer.MAX_VALUE) {
                            rules.put("maxlength", size.max());
                        }
                    }

                    Pattern pattern = field.getAnnotation(Pattern.class);
                    if (pattern != null) {
                        rules.put("regex", pattern.regexp());
                    }

                    if (rules.size() > 0) {
                        nameRules.put(name, rules);

                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return nameRules;
    }

    public static Map<String, String> buildRequestInfoDataMap(HttpServletRequest request, boolean verbose) {

        Map<String, String> dataMap = Maps.newLinkedHashMap();

        // Request相关的参数、属性等数据组装
        dataMap.put("req.user", ObjectUtils.toString(SecurityUtils.getSubject().getPrincipal()));
        dataMap.put("req.method", request.getMethod());
        dataMap.put(ClassicConstants.REQUEST_REQUEST_URI, request.getRequestURI());
        dataMap.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, request.getHeader("User-Agent"));

        String clientId = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(clientId)) {
            clientId = request.getRemoteHost();
        }
        dataMap.put("req.clientId", clientId);

        if (verbose) {
            dataMap.put(ClassicConstants.REQUEST_QUERY_STRING, request.getQueryString());
            dataMap.put("req.contextPath", request.getContextPath());
            dataMap.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());
            dataMap.put("req.remotePort", String.valueOf(request.getRemotePort()));
            dataMap.put("req.remoteUser", request.getRemoteUser());
            dataMap.put("req.localAddr", request.getLocalAddr());
            dataMap.put("req.localName", request.getLocalName());
            dataMap.put("req.localPort", String.valueOf(request.getLocalPort()));
            dataMap.put("req.serverName", request.getServerName());
            dataMap.put("req.serverPort", String.valueOf(request.getServerPort()));
            dataMap.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, request.getHeader("X-Forwarded-For"));
            dataMap.put(ClassicConstants.REQUEST_REQUEST_URL, request.getRequestURL().toString());
        }

        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = StringUtils.join(request.getParameterValues(paramName), ",");
            if (paramValue != null && paramValue.length() > 100) {
                paramValue = paramValue.substring(0, 100) + "...";
            }
            dataMap.put("req.param[" + paramName + "]", paramValue);
        }

        if (request instanceof MultipartHttpServletRequest) {
            // 转型为MultipartHttpRequest
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获得上传的文件（根据前台的name名称得到上传的文件）
            MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
            for (String key : multiValueMap.keySet()) {
                dataMap.put("req.part[" + key + "]", multiValueMap.getFirst(key).getName());
            }
        }

        if (verbose) {
            Enumeration<?> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                dataMap.put("req.header[" + headerName + "]", request.getHeader(headerName));
            }

            Enumeration<?> attrNames = request.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = (String) attrNames.nextElement();
                Object attrValue = request.getAttribute(attrName);
                if (attrValue == null) {
                    attrValue = "NULL";
                }
                String attr = attrValue.toString();
                if (attr != null && attr.toString().length() > 100) {
                    attr = attr.substring(0, 100) + "...";
                }
                dataMap.put("req.attr[" + attrName + "]", attr);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                Enumeration<?> sessionAttrNames = session.getAttributeNames();
                while (sessionAttrNames.hasMoreElements()) {
                    String attrName = (String) sessionAttrNames.nextElement();
                    Object attrValue = session.getAttribute(attrName);
                    if (attrValue == null) {
                        attrValue = "NULL";
                    }
                    String attr = attrValue.toString();
                    if (attr != null && attr.toString().length() > 100) {
                        attr = attr.toString().substring(0, 100) + "...";
                    }
                    dataMap.put("session.attr[" + attrName + "]", attr);
                }
            }
        }
        return dataMap;
    }

    private static String readFileUrlPrefix;

    /**
     * 文件显示URL前缀
     *
     * @return
     */
    public static String getReadFileUrlPrefix() {
        if (readFileUrlPrefix == null) {
            if (StringUtils.isBlank(readFileUrlPrefix)) {
                readFileUrlPrefix = WebAppContextInitFilter.getInitedWebContextFullUrl();
            }
        }
        return readFileUrlPrefix;
    }

    /**
     * 将URL进行解析处理，如果http打头直接返回，否则添加文件访问路径前缀
     *
     * @return
     */
    public static String parseReadFileUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (url.startsWith("http") || url.startsWith("itms-services://")) {
            return url;
        }
        return getReadFileUrlPrefix() + url;
    }

    /**
     * 将URL基于/切分，把路径中的中文部分做UTF8编码后再组装返回
     *
     * @return
     */
    public static String encodeUtf8Url(String url) {
        if (url == null) {
            return null;
        }
        String[] splits = url.split("/");
        List<String> urls = Lists.newArrayList();
        try {
            for (String split : splits) {
                if (StringUtils.isNotBlank(split)) {
                    urls.add(URLEncoder.encode(split, "UTF-8"));
                } else {
                    urls.add("");
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return StringUtils.join(urls, "/");
    }

    private static String staticFileUploadDir;

    /**
     * 获取文件上传根目录：优先取write_upload_file_dir参数值，如果没有定义则取webapp/upload
     *
     * @return 返回图片访问相对路径
     */
    public static String writeUploadFile(InputStream fis, String name, long length) {
        if (staticFileUploadDir == null) {
            if (StringUtils.isBlank(staticFileUploadDir)) {
                staticFileUploadDir = WebAppContextInitFilter.getInitedWebContextRealPath();
            }
            if (staticFileUploadDir.endsWith(File.separator)) {
                staticFileUploadDir = staticFileUploadDir.substring(0, staticFileUploadDir.length() - 1);
            }
            logger.info("Setup file upload root dir:  {}", staticFileUploadDir);
        }

        // 简便的做法用UUID作为主键，每次上传都会创建文件对象和数据记录，便于管理，但是存在相同文件重复保存情况
        String id = UUID.randomUUID().toString();

        // 加上年月日分组处理，一方面便于直观看出上传文件日期信息以便批量处理，另一方面合理分组控制目录的个数和层级避免单一目录下文件过多
        DateTime now = new DateTime();
        StringBuilder sb = new StringBuilder();
        int year = now.getYear();
        sb.append("/" + year);
        String month;
        int monthOfYear = now.getMonthOfYear();
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        } else {
            month = "" + monthOfYear;
        }
        String day;
        int dayOfMonth = now.getDayOfMonth();
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = "" + dayOfMonth;
        }
        sb.append("/" + month);
        sb.append("/" + day);
        Assert.notNull(id, "id is required to buildInstance");
        sb.append("/" + id);

        String path = "/upload" + sb + "/" + name;
        String fullPath = staticFileUploadDir + path;
        logger.debug("Saving upload file: {}", fullPath);
        try {
            FileUtils.copyInputStreamToFile(fis, new File(fullPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;

    }

    public static boolean isMobileAndroidClient(HttpServletRequest request) {
        String android = request.getParameter("_android_");
        if (BooleanUtils.toBoolean(android)) {
            return true;
        }
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        if (userAgent.toLowerCase().contains("android")) {
            return true;
        }
        return false;
    }

    public static boolean isMobileIOSClient(HttpServletRequest request) {
        String ios = request.getParameter("_ios_");
        if (BooleanUtils.toBoolean(ios)) {
            return true;
        }
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
            return true;
        }
        return false;
    }

    public static boolean isMobileClient(HttpServletRequest request) {
        // 特殊参数处理
        String mobile = request.getParameter("_mobile_");
        if (BooleanUtils.toBoolean(mobile)) {
            return true;
        }
        String userAgent = request.getHeader("USER-AGENT");
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMicroMessengerClient(HttpServletRequest request) {
        // 特殊参数处理
        String mobile = request.getParameter("_weixin_");
        if (BooleanUtils.toBoolean(mobile)) {
            return true;
        }
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("micromessenger")) {
            return true;
        }

        return false;
    }

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // logger.debug("Cookie: {}={}", cookie.getName(),
                // cookie.getValue());
                if (key.equals(cookie.getName())) {
                    return Encodes.urlDecode(cookie.getValue());
                }
            }
        }
        // 补偿从request属性获取
        Object value = request.getAttribute(key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, Encodes.urlEncode(value));
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
        // 补偿记录到request属性，便于后续逻辑能立即获取值
        request.setAttribute(key, value);
    }

    public static String getRequestFullURL(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getScheme()).append("://").append(request.getServerName());
        sb.append(request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
        sb.append(request.getContextPath());
        sb.append(request.getServletPath());
        String query = request.getQueryString();
        if (StringUtils.isNotBlank(query)) {
            sb.append("?" + query);
        }
        return sb.toString();
    }

    /**
     * 从HTTP请求提取AccessTkoen
     *
     * @param request
     * @return
     */
    public static String getOAuthAccessToken(HttpServletRequest request) {
        return request.getHeader(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
    }
}
