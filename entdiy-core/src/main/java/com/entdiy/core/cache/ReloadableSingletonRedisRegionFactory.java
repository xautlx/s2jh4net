/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.cache;

import com.entdiy.core.context.SpringPropertiesHolder;
import org.hibernate.cache.redis.hibernate52.SingletonRedisRegionFactory;
import org.hibernate.cache.redis.util.Timestamper;
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
        if (cacheTimestamper == null) {
            cacheTimestamper = new Timestamper();
        }
        return cacheTimestamper.next();
    }
}
