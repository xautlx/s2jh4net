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
package com.entdiy.core.cons;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.EnumKeyLabelPair;
import com.google.common.collect.Maps;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class GlobalConstant {

    public static final String ROOT_VALUE = "root";
    public static final String DEFAULT_VALUE = "default";
    public static final String NONE_VALUE = "none";

    public static final String API_MAPPING_PREFIX = "/api";

    /**
     * 对于一些复杂处理逻辑需要基于提交数据服务器校验后有提示警告信息需要用户二次确认
     * 标识服务端校验用户提交确认的参数名称，判断当前表单是否已被用户confirm确认OK
     */
    public static final String PARAM_KEY_USER_CONFIRMED = "_serverValidationConfirmed_";

    public static final Map<Boolean, String> booleanLabelMap = Maps.newLinkedHashMap();

    static {
        booleanLabelMap.put(Boolean.TRUE, "是");
        booleanLabelMap.put(Boolean.FALSE, "否");
    }

    @MetaData("性别枚举定义")
    public enum GenderEnum implements EnumKeyLabelPair {
        M {
            @Override
            public String getLabel() {
                return "男";
            }
        },

        F {
            @Override
            public String getLabel() {
                return "女";
            }
        },

        S {
            @Override
            public String getLabel() {
                return "保密";
            }
        }
    }

    @MetaData("OAuth认证类型")
    public enum OauthTypeEnum implements EnumKeyLabelPair {
        MOCK {
            @Override
            public String getLabel() {
                return "模拟";
            }
        },

        WECHAT {
            @Override
            public String getLabel() {
                return "微信";
            }
        },

        ALIPAY {
            @Override
            public String getLabel() {
                return "支付宝";
            }
        }
    }

    @MetaData("类似OAuth的APP认证AccessToken请求Header名称")
    public final static String APP_AUTH_ACCESS_TOKEN = "Access-Token";

    @MetaData("标识APP端语言选项，形如en-US|zh-CN|zh-TW|ja-JP等，用于服务端必要的国际化处理")
    public final static String APP_LOCALE = "App-Locale";

    @MetaData("ConfigProperty:是否全局禁用开放手机号短信发送功能")
    public final static String CFG_SMS_DISABLED = "sms.disabled";

    @MetaData(value = "ConfigProperty:系统管理员邮箱地址列表", comments = "主要用于接收一些系统层面的重要通知、异常等邮件")
    public final static String CFG_SYSTEM_EMAILS = "system.emails";

    @MetaData("ConfigProperty:静态资源访问URI前缀列表，多个以逗号分隔")
    public static final String CFG_UPLOAD_PUBLIC_RESOURCE_URI = "upload.public.resource.uri";

    @MetaData("DataDict:消息类型")
    public final static String DATADICT_MESSAGE_TYPE = "MESSAGE_TYPE";

    /**
     * 兼容MySQL的最大时间值。直接用LocalDateTime.MAX会导致MySQL存储异常。
     */
    public final static LocalDateTime MAX_LOCAL_DATE_TIME = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    /**
     * 兼容MySQL的最大日期值。直接用LocalDate.MAX会导致MySQL存储异常。
     */
    public final static LocalDate MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);
}
