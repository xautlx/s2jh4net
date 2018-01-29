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
package com.entdiy.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * 实体对象相关缓存服务。
 * 注意：同一个Bean类中非@Cacheable方法调用@Cacheable将失效，
 * 因此遇到此类需求需要在外部定义一个Service来实现不同Bean Service之间调用
 */
@Service
public class EntityCacheService implements AppCacheService {

    private static Logger logger = LoggerFactory.getLogger(EntityCacheService.class);

    private static final String CACHE_NAME = "entity_cache";

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void cacheEvictAll() {
        logger.info("Spring CacheEvict ALL for cache: {}", CACHE_NAME);
    }
}
