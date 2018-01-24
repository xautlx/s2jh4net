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
package com.entdiy.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 基于缓存服务接口的类名、方法名、参数信息构造缓存Key
 *
 * @see SimpleKeyGenerator#generateKey(Object...)
 */
@Component
public class MethodCacheKeyGenerator implements KeyGenerator {

    private static Logger logger = LoggerFactory.getLogger(MethodCacheKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append("spring_cache:");
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName());
        if (params != null && params.length > 0) {
            key.append("(" + StringUtils.arrayToCommaDelimitedString(params) + ")");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("using cache key={}", key);
        }
        return key.toString();
    }
}
