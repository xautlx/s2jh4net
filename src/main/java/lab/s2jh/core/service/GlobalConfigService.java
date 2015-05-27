package lab.s2jh.core.service;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.web.listener.ApplicationContextPreListener;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class GlobalConfigService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalConfigService.class);

    @MetaData(value = "开发模式", comments = "更宽松的权限控制，更多的日志信息。详见application.properties配置参数定义")
    private static boolean devMode = false;

    @MetaData(value = "演示模式", comments = "对演示环境进行特殊控制以避免不必要的随意数据修改导致系统混乱")
    private static boolean demoMode = false;

    @MetaData(value = "构建版本")
    private static String buildVersion;

    public static boolean isDemoMode() {
        return demoMode;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    @Value("${build_version}")
    public void setBuildVersion(String buildVersion) {
        GlobalConfigService.buildVersion = buildVersion;
        logger.info("System runnging at build_version={}", GlobalConfigService.buildVersion);
    }

    @Value("${demo_mode:false}")
    public void setDemoMode(String demoMode) {
        GlobalConfigService.demoMode = BooleanUtils.toBoolean(demoMode);
        logger.info("System runnging at demo_mode={}", GlobalConfigService.demoMode);
    }

    @Value("${dev_mode:false}")
    public void setDevMode(String devMode) {
        GlobalConfigService.devMode = BooleanUtils.toBoolean(devMode);
        logger.info("System runnging at dev_mode={}", GlobalConfigService.devMode);
    }

    public static String getBuildVersion() {
        return buildVersion;
    }
}
