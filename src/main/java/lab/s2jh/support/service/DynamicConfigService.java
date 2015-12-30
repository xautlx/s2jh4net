package lab.s2jh.support.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.context.ExtPropertyPlaceholderConfigurer;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.service.ConfigPropertyService;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 基于数据库加载动态配置参数
 * 框架扩展属性加载：Spring除了从.properties加载属性数据
 * 并且数据库如果存在同名属性则优先取数据库的属性值覆盖配置文件中的值
 * 为了避免意外的数据库配置导致系统崩溃，约定以cfg打头标识的参数表示可以被数据库参数覆写，其余的则不会覆盖文件定义的属性值
 */
@Component
public class DynamicConfigService {

    private final Logger logger = LoggerFactory.getLogger(DynamicConfigService.class);

    @Autowired(required = false)
    private ExtPropertyPlaceholderConfigurer extPropertyPlaceholderConfigurer;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @MetaData(value = "开发模式", comments = "更宽松的权限控制，更多的日志信息。详见application.properties配置参数定义")
    private static boolean devMode = false;

    @MetaData(value = "演示模式", comments = "对演示环境进行特殊控制以避免不必要的随意数据修改导致系统混乱")
    private static boolean demoMode = false;

    @MetaData(value = "构建版本")
    private static String buildVersion;

    @MetaData(value = "构建时间")
    private static String buildTimestamp;

    public static boolean isDemoMode() {
        return demoMode;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    public static String getBuildVersion() {
        return buildVersion;
    }

    @Value("${build_version}")
    public void setBuildVersion(String buildVersion) {
        DynamicConfigService.buildVersion = buildVersion;
        logger.info("System runnging at build_version={}", DynamicConfigService.buildVersion);
    }

    @Value("${build_timestamp:}")
    public void setBuildTimestamp(String buildTimestamp) {
        if (StringUtils.isBlank(buildTimestamp)) {
            buildTimestamp = DateUtils.formatTimeNow();
        }
        DynamicConfigService.buildTimestamp = buildTimestamp;
        logger.info("System runnging at build_timestamp={}", DynamicConfigService.buildTimestamp);
    }

    @Value("${demo_mode:false}")
    public void setDemoMode(String demoMode) {
        DynamicConfigService.demoMode = BooleanUtils.toBoolean(demoMode);
        logger.info("System runnging at demo_mode={}", DynamicConfigService.demoMode);
    }

    @Value("${dev_mode:false}")
    public void setDevMode(String devMode) {
        DynamicConfigService.devMode = BooleanUtils.toBoolean(devMode);
        logger.info("System runnging at dev_mode={}", DynamicConfigService.devMode);
    }

    /**
     * 根据key获取对应动态参数值
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * 根据key获取对应动态参数值，如果没有则返回defaultValue
     */
    public String getString(String key, String defaultValue) {
        String val = null;
        //cfg打头参数，首先从数据库取值
        if (key.startsWith("cfg")) {
            ConfigProperty cfg = configPropertyService.findByPropKey(key);
            if (cfg != null) {
                val = cfg.getSimpleValue();
            }
        }

        //从环境变量获取
        if (val == null) {
            val = System.getProperty(key);
        }

        //未取到则继续从Spring属性文件定义取
        if (val == null) {
            if (extPropertyPlaceholderConfigurer != null) {
                val = extPropertyPlaceholderConfigurer.getProperty(key);
            } else {
                logger.warn("当前不是以ExtPropertyPlaceholderConfigurer扩展模式定义，因此无法加载获取Spring属性配置");
            }
        }
        if (val == null) {
            logger.warn("Undefined config property for: {}", key);
            return defaultValue;
        } else {
            return val.trim();
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return BooleanUtils.toBoolean(getString(key, String.valueOf(defaultValue)));
    }

    public Map<String, String> getAllPrperties() {
        Map<String, String> properties = extPropertyPlaceholderConfigurer.getPropertiesMap();
        List<ConfigProperty> configProperties = configPropertyService.findAllCached();
        for (ConfigProperty configProperty : configProperties) {
            properties.put(configProperty.getPropKey(), configProperty.getSimpleValue());
        }
        return properties;
    }
}
