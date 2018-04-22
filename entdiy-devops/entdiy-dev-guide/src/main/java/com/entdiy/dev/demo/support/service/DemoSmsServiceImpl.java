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
package com.entdiy.dev.demo.support.service;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.support.service.SmsService;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DemoSmsServiceImpl implements SmsService {

    private final Logger logger = LoggerFactory.getLogger(DemoSmsServiceImpl.class);

    @Value("${sms.signature:}")
    private String smsSignature = "";

    @MetaData(value = "模拟发送模式，此模式下直接在console打印短信内容不进行实际短信接口调用")
    @Value("${sms_mock_mode:false}")
    private String smsMockMode;

    private boolean bSmsMockMode;

    @PostConstruct
    public void init() {
        bSmsMockMode = BooleanUtils.toBoolean(smsMockMode);
        if (bSmsMockMode) {
            logger.warn("---SMS Service running at MOCK mode---");
        }
    }

    /**
     * 短信发送接口
     * @param smsContent 短信内容
     * @param mobileNum 手机号码
     * 
     * @return 如果成功则返回null；否则失败返回异常消息
     */
    @Override
    public String sendSMS(String smsContent, String mobileNum, SmsMessageTypeEnum smsType) {
        if (mobileNum == null || mobileNum.length() != 11 || !mobileNum.startsWith("1")) {
            String message = "Invalid mobile number：" + mobileNum;
            logger.warn(message);
            return message;
        }

        //追加签名信息
        smsContent += smsSignature;

        if (bSmsMockMode) {
            logger.warn("SMS Service running at MOCK mode, just print SMS content:" + smsContent);
            return null;
        }

        logger.debug("Sending SMS to {} ： {}", mobileNum, smsContent);

        //TODO 实际短信通道接口调用发送
        return null;
    }
}
