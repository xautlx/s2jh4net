package com.entdiy.support.web;

import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.support.service.DynamicConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import java.util.Date;

public class RequestUtil {

    private final static Logger logger = LoggerFactory.getLogger(RequestUtil.class);


    private static DynamicConfigService dynamicConfigService;

    private static DynamicConfigService initDynamicConfigService() {
        if (dynamicConfigService == null) {
            dynamicConfigService = SpringContextHolder.getBean(DynamicConfigService.class);
        }
        return dynamicConfigService;
    }

    public static void appendGlobalProperties(Model model) {
        initDynamicConfigService();
        if (GlobalConfigService.isDevMode()) {
            // 开发模式，把版本信息设置为当前时间戳以触发JS、CSS等资源刷新加载
            model.addAttribute("buildVersion", new Date().getTime());
        } else {
            model.addAttribute("buildVersion", GlobalConfigService.getBuildVersion());
        }
        model.addAttribute("devMode", GlobalConfigService.isDevMode());
        model.addAttribute("systemTitle", dynamicConfigService.getString("cfg_system_title"));
    }


}
