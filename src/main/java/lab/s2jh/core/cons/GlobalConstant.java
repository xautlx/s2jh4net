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

    /**
     * 用于全局控制Hibernate的外键生成策略，定义为none表示默认不生成外键一般用于开发阶段方便数据表重建，空白则一般用于生产环境正常自动生成外键定义
     */
    public final static String GlobalForeignKeyName = "none";
}
