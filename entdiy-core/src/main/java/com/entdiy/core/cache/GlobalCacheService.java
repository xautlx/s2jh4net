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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 凡是涉及到集群支持的参数数据，可通过当前服务进行管理，最终依靠中央缓存机制实现所有节点数据保持一致。
 * 如果是单机缓存信息，可以采用传统的静态类实现，如buildVersion
 */
@Service
public class GlobalCacheService {

    private static Logger logger = LoggerFactory.getLogger(GlobalCacheService.class);


}
