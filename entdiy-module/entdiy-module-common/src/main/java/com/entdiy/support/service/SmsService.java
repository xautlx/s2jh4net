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

    public static enum SmsMessageTypeEnum {
        @MetaData(value = "缺省", comments = "一般是程序触发，不做限制的短信发送")
        Default,

        @MetaData(value = "注册", comments = "用于注册时发送短信，限制条件为 间隔不能小于一分钟，一个小时内不能超过10次")
        Signup,

        @MetaData(value = "验证码", comments = "用于发送短信验证码，限制条件为 间隔不能小于一分钟，一个小时内不能超过10次")
        VerifyCode;

    }
}
