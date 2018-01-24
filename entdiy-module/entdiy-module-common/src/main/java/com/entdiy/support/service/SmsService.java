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
package com.entdiy.support.service;

import com.entdiy.core.annotation.MetaData;

public interface SmsService {
    /**
     * 短信发送接口
     * @param smsContent 短信内容
     * @param mobileNum 手机号码
     *
     * @return 如果成功则返回null；否则失败返回异常消息
     */
    String sendSMS(String smsContent, String mobileNum, SmsMessageTypeEnum smsType);

    enum SmsMessageTypeEnum {
        @MetaData(value = "缺省", comments = "一般是程序触发，不做限制的短信发送")
        Default,

        @MetaData(value = "注册", comments = "用于注册时发送短信，限制条件为 间隔不能小于一分钟，一个小时内不能超过10次")
        Signup,

        @MetaData(value = "验证码", comments = "用于发送短信验证码，限制条件为 间隔不能小于一分钟，一个小时内不能超过10次")
        VerifyCode;

    }
}
