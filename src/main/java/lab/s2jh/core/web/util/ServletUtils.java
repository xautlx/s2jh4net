package lab.s2jh.core.web.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.GlobalConfigService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.Digests;
import lab.s2jh.core.util.Encodes;
import lab.s2jh.core.web.filter.WebAppContextInitFilter;
import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lab.s2jh.module.sys.entity.AttachmentFile;
import lab.s2jh.module.sys.service.AttachmentFileService;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ServletUtils {

    private final static Logger logger = LoggerFactory.getLogger(ServletUtils.class);

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * 
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> buildParameters(ServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
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
     * 设置让浏览器弹出下载对话框的Header.
     * 
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            //中文文件名支持
            String encodedfileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static void renderFileDownload(HttpServletResponse response, byte[] fileData) {
        byte[] buffer = new byte[4096];
        BufferedOutputStream output = null;
        ByteArrayInputStream input = null;
        try {
            output = new BufferedOutputStream(response.getOutputStream());
            input = new ByteArrayInputStream(fileData);

            int n = (-1);
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                output.write(buffer, 0, n);
            }
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private static Map<Class<?>, String> entityValidationIdMap = Maps.newHashMap();
    private static Map<String, Map<String, Object>> entityValidationRulesMap = Maps.newHashMap();

    /**
     * 基于JSR注解以AJAX方式请求实体对象对应的校验规则JSON，前端需要传入一个实体对象的标识
     * 为了避免在响应用户的HTML内容中直接透露实体全名等敏感信息，做一个转义处理：
     * 基于class对象转换得到一个哈希字符串，前端通过此哈希字符串进行参数标识
     */
    public static String buildValidateId(Class<?> entityClass) {
        String id = entityValidationIdMap.get(entityClass);
        if (id == null) {
            id = Encodes.encodeHex(Digests.md5(entityClass.getName().getBytes()));
            entityValidationIdMap.put(entityClass, id);
        }
        return id;
    }

    /**
     * 基于构建的哈希标识计算获取校验规则
     * @param id
     * @return
     */
    public static Map<String, Object> buildValidateRules(String id) {
        Map<String, Object> nameRules = entityValidationRulesMap.get(id);
        try {
            //开发模式则每次计算以便修改注解后及时生效
            if (nameRules == null || GlobalConfigService.isDevMode()) {
                nameRules = Maps.newHashMap();
                entityValidationRulesMap.put(id, nameRules);
                Class<?> clazz = null;
                for (Map.Entry<Class<?>, String> me : entityValidationIdMap.entrySet()) {
                    if (me.getValue().equals(id)) {
                        clazz = me.getKey();
                        break;
                    }
                }
                Assert.notNull(clazz, "验证缓存数据错误");
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

                    //TODO 优化嵌套属性处理
                    //如果是实体对象类型，一般表单元素name都定义为entity.id，因此额外追加对应id属性校验规则
                    //if (PersistableEntity.class.isAssignableFrom(field.getType())) {
                    //    nameRules.put(name + ".id", rules);
                    //}

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
                            //rules.put("unique", true);
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

    private static final Integer PAD_SIZE = 30;

    public static String buildRequestInfoToString(HttpServletRequest request, boolean verbose) {
        //Request相关的参数、属性等数据组装
        StringBuilder sb = new StringBuilder();
        String xForwardedFor = request.getHeader("x-forwarded-for");
        sb.append(StringUtils.rightPad("\nHTTP Request Logon User PIN", PAD_SIZE) + ":" + AuthContextHolder.getAuthUserDisplay());
        if (verbose) {
            sb.append(StringUtils.rightPad("\nHTTP Request RemoteAddr", PAD_SIZE) + ":" + request.getRemoteAddr());
            sb.append(StringUtils.rightPad("\nHTTP Request RemoteHost", PAD_SIZE) + ":" + request.getRemoteHost());
            sb.append(StringUtils.rightPad("\nHTTP Request x-forwarded-for", PAD_SIZE) + ":" + xForwardedFor);
        }
        sb.append(StringUtils.rightPad("\nHTTP Request Method", PAD_SIZE) + ":" + request.getMethod());
        sb.append(StringUtils.rightPad("\nHTTP Request URI", PAD_SIZE) + ":" + request.getRequestURI());
        sb.append(StringUtils.rightPad("\nHTTP Request Query String", PAD_SIZE) + ":" + request.getQueryString());

        sb.append("\nHTTP Request Parameter List : ");
        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = StringUtils.join(request.getParameterValues(paramName), ",");
            if (paramValue != null && paramValue.length() > 100) {
                sb.append("\n - " + paramName + "=" + paramValue.substring(0, 100) + "...");
            } else {
                sb.append("\n - " + paramName + "=" + paramValue);
            }
        }

        if (verbose) {
            sb.append("\nRequest Header Data:");
            Enumeration<?> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                sb.append("\n - " + headerName + "=" + request.getHeader(headerName));
            }

            sb.append("\nRequest Attribute Data:");
            Enumeration<?> attrNames = request.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = (String) attrNames.nextElement();
                Object attr = request.getAttribute(attrName);
                if (attr != null && attr.toString().length() > 100) {
                    sb.append("\n - " + attrName + "=" + attr.toString().substring(0, 100) + "...");
                } else {
                    sb.append("\n - " + attrName + "=" + attr);
                }
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                sb.append("\nSession Attribute Data:");

                Enumeration<?> sessionAttrNames = session.getAttributeNames();
                while (sessionAttrNames.hasMoreElements()) {
                    String attrName = (String) sessionAttrNames.nextElement();
                    Object attr = session.getAttribute(attrName);
                    if (attr != null && attr.toString().length() > 100) {
                        sb.append("\n - " + attrName + "=" + attr.toString().substring(0, 100) + "...");
                    } else {
                        sb.append("\n - " + attrName + "=" + attr);
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String readFileUrlPrefix;

    /**
     * 文件显示URL前缀
     * @return
     */
    public static String getReadFileUrlPrefix() {
        if (readFileUrlPrefix == null) {
            DynamicConfigService dynamicConfigService = SpringContextHolder.getBean(DynamicConfigService.class);
            readFileUrlPrefix = dynamicConfigService.getString("read_file_url_prefix");
            if (StringUtils.isBlank(readFileUrlPrefix)) {
                readFileUrlPrefix = WebAppContextInitFilter.getInitedWebContextFullUrl();
            }
        }
        return readFileUrlPrefix;
    }

    private static String staticFileUploadDir;

    /**
     * 获取文件上传根目录：优先取write_upload_file_dir参数值，如果没有定义则取webapp/upload
     * @return 返回图片显示的完整URL
     */
    public static String writeUploadFile(InputStream fis, String name, long length) {
        if (staticFileUploadDir == null) {
            DynamicConfigService dynamicConfigService = SpringContextHolder.getBean(DynamicConfigService.class);
            staticFileUploadDir = dynamicConfigService.getString("write_upload_file_dir");
            if (StringUtils.isBlank(staticFileUploadDir)) {
                staticFileUploadDir = WebAppContextInitFilter.getInitedWebContextRealPath();
            }
            if (staticFileUploadDir.endsWith(File.separator)) {
                staticFileUploadDir = staticFileUploadDir.substring(0, staticFileUploadDir.length() - 1);
            }
            logger.info("Setup file upload root dir:  {}", staticFileUploadDir);
        }

        try {

            AttachmentFile attachmentFile = AttachmentFile.buildInstance(name, length);
            String path = "/upload" + attachmentFile.getFileRelativePath() + "/" + attachmentFile.getDiskFileName();
            String fullPath = staticFileUploadDir + path;
            logger.debug("Saving upload file: {}", fullPath);
            FileUtils.copyInputStreamToFile(fis, new File(fullPath));

            AttachmentFileService attachmentFileServiceService = SpringContextHolder.getBean(AttachmentFileService.class);
            attachmentFileServiceService.save(attachmentFile);

            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
