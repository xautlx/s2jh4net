/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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
package com.entdiy.locale.web;

import org.springframework.core.NamedThreadLocal;

/**
 * 基于ThreadLocal把Request相关数据注入请求线程，后续业务逻辑层使用
 */
public class RequestLocaleHolder {

    /** 标识APP端语言选项，形如en-US|zh-CN|zh-TW|ja-JP等，用于服务端必要的国际化处理 */
    private static ThreadLocal<String> requestLocale = new NamedThreadLocal("RequestLocale");

    public static void setRequestLocale(String locale) {
        requestLocale.set(locale);
    }

    public static String getRequestLocale() {
        return requestLocale.get();
    }

    public static void clear() {
        requestLocale.remove();
    }
}
