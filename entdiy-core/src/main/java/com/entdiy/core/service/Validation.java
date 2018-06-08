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
package com.entdiy.core.service;

import com.entdiy.core.exception.ValidationException;
import com.entdiy.core.web.AppContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 用于业务逻辑校验的“断言”控制，与常规的Assert断言区别在于抛出 @see ValidationException
 * 此类异常不会进行常规的logger.error记录，一般只在前端显示提示用户
 */
public class Validation {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ValidationException(message);
        }
    }

    public static void assertAuthDataAccess(boolean expression) {
        if (!expression) {
            throw new ValidationException("数据访问未授权");
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }

    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ValidationException(message);
        }
    }

    /**
     * 如果是演示模式并且是POST请求，则拒绝以避免用户随意修改数据导致演示不完整
     *
     * @param request
     */
    public static void notDemoMode(HttpServletRequest request) {
        if (AppContextHolder.isDemoMode()) {
            if (request == null || HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
                throw new ValidationException("抱歉，此功能在演示模式被禁用，请参考文档在本地部署运行体验。");
            }
        }
    }

    /**
     * 如果是演示模式则拒绝以避免用户随意修改数据导致演示不完整
     */
    public static void notDemoMode() {
        notDemoMode(null);
    }
}
