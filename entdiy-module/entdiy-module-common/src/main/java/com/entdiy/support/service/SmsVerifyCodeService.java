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

import com.entdiy.core.service.Validation;
import com.entdiy.core.web.AppContextHolder;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsVerifyCodeService {

    private static final Logger logger = LoggerFactory.getLogger(SmsVerifyCodeService.class);

    @Value("${sms.code.expire.seconds:300}")
    private Integer smsCodeExpireSeconds;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private SmsService smsService;

    /**
     * 基于手机号码生成6位随机验证码
     *
     * @param templateCode 短信模板代码
     * @param mobileNum    手机号码
     * @return 布尔类型是否成功
     */
    public boolean sendSmsCode(String templateCode, String mobileNum) {
        String random = RandomStringUtils.randomNumeric(6);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(mobileNum, random, smsCodeExpireSeconds, TimeUnit.SECONDS);//5分钟过期
        Map<String, Object> data = Maps.newHashMap();
        data.put("code", random);
        data.put("minutes", Integer.valueOf(smsCodeExpireSeconds / 60));

        String result = smsService.sendSMS(null, templateCode, data, mobileNum);
        return StringUtils.isBlank(result);
    }

    /**
     * 验证手机验证码有效性
     *
     * @param mobileNum 手机号码
     * @param inputCode 验证码
     * @return 布尔类型是否有效
     */
    public boolean verifySmsCode(String mobileNum, String inputCode) {
        Validation.notBlank(inputCode, "验证码不能为空");

        //如果是开发模式，则123456作为默认验证码始终通过，方便开发测试
        if (AppContextHolder.isDevMode()) {
            if ("123456".equals(inputCode)) {
                return true;
            }
        }

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String code = ops.get(mobileNum);
        return inputCode.equals(code);
    }
}
