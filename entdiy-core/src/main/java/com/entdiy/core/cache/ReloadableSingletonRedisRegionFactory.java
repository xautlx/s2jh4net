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

import com.entdiy.core.context.SpringPropertiesHolder;
import com.entdiy.core.web.AppContextHolder;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.redis.hibernate52.SingletonRedisRegionFactory;
import org.hibernate.cache.redis.hibernate52.regions.RedisQueryResultsRegion;
import org.hibernate.cache.redis.util.Timestamper;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 在JRebel运行模式，热更新类后，默认的SingletonRedisRegionFactory会频繁抛出cacheTimestamper为null异常
 * 因此覆写基类nextTimestamp动态创建cacheTimestamper提高开发效率
 */
public class ReloadableSingletonRedisRegionFactory extends SingletonRedisRegionFactory {

    private static Logger logger = LoggerFactory.getLogger(SpringPropertiesHolder.class);

    public ReloadableSingletonRedisRegionFactory(Properties props) {
        super(props);
        logger.info("Create ReloadableSingletonRedisRegionFactory instance.");
    }

    @Override
    public long nextTimestamp() {
        if (cacheTimestamper == null && AppContextHolder.isDevMode()) {
            cacheTimestamper = new Timestamper();
        }
        return cacheTimestamper.next();
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName,
                                                      Properties properties) throws CacheException {
        return new RedisQueryResultsRegion(accessStrategyFactory,
                redis,
                this,
                //追加前缀以便版本滚动更新时互补干扰
                properties.get("hibernate.cache.region_prefix") + regionName,
                properties);
    }
}
