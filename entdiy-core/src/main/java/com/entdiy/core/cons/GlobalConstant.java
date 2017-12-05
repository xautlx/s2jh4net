package com.entdiy.core.cons;

import java.util.Map;

import com.entdiy.core.annotation.MetaData;

import com.google.common.collect.Maps;

public class GlobalConstant {

    public static final Map<Boolean, String> booleanLabelMap = Maps.newLinkedHashMap();

    static {
        booleanLabelMap.put(Boolean.TRUE, "是");
        booleanLabelMap.put(Boolean.FALSE, "否");
    }

    //性别  
    public static enum GenderEnum {
        @MetaData(value = "男")
        M,

        @MetaData(value = "女")
        F,

        @MetaData(value = "保密")
        S,

        @MetaData(value = "未知")
        U;
    }

    @MetaData("类似OAuth的APP认证AccessToken请求Header名称")
    public final static String APP_AUTH_ACCESS_TOKEN = "ACCESS-TOKEN";

    @MetaData("ConfigProperty:系统标题名称")
    public final static String cfg_system_title = "cfg_system_title";

    @MetaData("ConfigProperty:是否禁用管理账号注册功能")
    public final static String cfg_mgmt_signup_disabled = "cfg_mgmt_signup_disabled";

    @MetaData("ConfigProperty:是否全局禁用开放手机号短信发送功能")
    public final static String cfg_public_send_sms_disabled = "cfg_public_send_sms_disabled";

    @MetaData("数据字典:消息类型")
    public final static String DataDict_Message_Type = "DataDict_Message_Type";

}
