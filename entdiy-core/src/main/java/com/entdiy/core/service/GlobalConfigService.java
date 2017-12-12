/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.service;

import com.entdiy.core.annotation.MetaData;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public static String getBuildVersion() {
        //开发模式，每次返回不同值，以强制更新加载JS等静态资源方便开发调试
        if (isDevMode()) {
            return String.valueOf(System.currentTimeMillis());
        } else {
            return buildVersion;
        }
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
}
