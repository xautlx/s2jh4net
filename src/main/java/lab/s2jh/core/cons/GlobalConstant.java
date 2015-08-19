package lab.s2jh.core.cons;

import java.util.Map;

import lab.s2jh.core.annotation.MetaData;

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
        U;
    }

    @MetaData("ConfigProperty:是否禁用管理账号注册功能")
    public final static String cfg_signup_disabled = "cfg_signup_disabled";

    @MetaData("数据字典:消息类型")
    public final static String DataDict_Message_Type = "DataDict_Message_Type";
}
