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
package com.entdiy.support.web;

import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.support.service.DynamicConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用请求帮助类
 */
public class RequestUtil {

    private final static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private static DynamicConfigService dynamicConfigService;

    private static DynamicConfigService initDynamicConfigService() {
        if (dynamicConfigService == null) {
            logger.debug("Init dynamicConfigService from SpringContextHolder...");
            dynamicConfigService = SpringContextHolder.getBean(DynamicConfigService.class);
        }
        return dynamicConfigService;
    }

    public static void appendGlobalProperties(HttpServletRequest request, Model model) {
        initDynamicConfigService();
        if (GlobalConfigService.isDevMode()) {
            // 开发模式，把版本信息设置为当前时间戳以触发JS、CSS等资源刷新加载
            model.addAttribute("buildVersion", System.currentTimeMillis());
        } else {
            model.addAttribute("buildVersion", GlobalConfigService.getBuildVersion());
        }
        model.addAttribute("devMode", GlobalConfigService.isDevMode());
        model.addAttribute("systemTitle", dynamicConfigService.getString("cfg_system_title"));

        //KindEditor文件操作涉及到Flash集成，因此需要在upload URL追加JSESSIONID参数以进行登录用户标识传递
        model.addAttribute("fileUploadUrl", "/pub/file-upload/kind-editor.json;JSESSIONID=" + request.getSession().getId());
    }


}
