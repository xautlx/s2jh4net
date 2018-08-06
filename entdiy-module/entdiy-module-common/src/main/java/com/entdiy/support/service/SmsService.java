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

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.Validation;
import com.entdiy.core.web.AppContextHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public abstract class SmsService {

    Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private DynamicConfigService dynamicConfigService;


    @Value("${sms.signature:}")
    private String defaultSignature = "";

    /**
     * 短信发送接口
     *
     * @param signature      短信签名，如果签名为空则取默认签名配置
     * @param templateCode   短信模板代码
     * @param templateParams 模板替换参数
     * @param mobileNums     手机号，视不同短信服务商一般限制最大个数，如阿里云不超过100
     * @return 如果成功则返回null；否则失败返回异常消息
     */
    public String sendSMS(String signature, String templateCode, Map<String, Object> templateParams, String... mobileNums) {
        if (dynamicConfigService.getBoolean(GlobalConstant.CFG_SMS_DISABLED, false)) {
            throw new ServiceException("短信发送功能已暂停");
        }

        if (StringUtils.isBlank(signature)) {
            signature = defaultSignature;
        }
        Validation.isTrue(StringUtils.isNotBlank(templateCode), "短信模板代码必须");
        Validation.isTrue(ArrayUtils.isNotEmpty(mobileNums), "短信号码必须");

        if (logger.isDebugEnabled()) {
            logger.debug("Sending SMS:");
            logger.debug(" - signature: {}", signature);
            logger.debug(" - templateCode: {}", templateCode);
            //出于安全考虑，不在生产环境控制台输出敏感信息，如验证码等
            if (AppContextHolder.isDevMode() || AppContextHolder.isDemoMode()) {
                logger.warn(" - templateParams: {}", templateParams);
            }
            logger.debug(" - mobileNums: {}", mobileNums);
        }

        return sendSMSByVendor(signature, templateCode, templateParams, mobileNums);
    }

    protected abstract String sendSMSByVendor(String signature, String templateCode, Map<String, Object> templateParams, String... mobileNums);
}
