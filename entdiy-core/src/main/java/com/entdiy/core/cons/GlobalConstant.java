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
import com.google.common.collect.Maps;

import java.util.Map;

public class GlobalConstant {

    public static final String ROOT_VALUE = "root";
    public static final String DEFAULT_VALUE = "default";
    public static final String NONE_VALUE = "none";

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

    //性别  
    public enum GenderEnum {
        @MetaData(value = "男")
        M,

        @MetaData(value = "女")
        F,

        @MetaData(value = "保密")
        S
    }

    @MetaData("类似OAuth的APP认证AccessToken请求Header名称")
    public final static String APP_AUTH_ACCESS_TOKEN = "ACCESS-TOKEN";

    @MetaData("ConfigProperty:是否全局禁用开放手机号短信发送功能")
    public final static String CFG_SMS_DISABLED = "SMS_DISABLED";

    @MetaData("DataDict:消息类型")
    public final static String DATADICT_MESSAGE_TYPE = "MESSAGE_TYPE";
}
