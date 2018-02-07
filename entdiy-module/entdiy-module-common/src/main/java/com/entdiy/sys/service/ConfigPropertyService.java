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
package com.entdiy.sys.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.ConfigPropertyDao;
import com.entdiy.sys.entity.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConfigPropertyService extends BaseService<ConfigProperty, Long> {

    @Autowired
    private ConfigPropertyDao configPropertyDao;

    public List<ConfigProperty> findAllCached() {
        return configPropertyDao.findAllCached();
    }

    @Override
    protected BaseDao<ConfigProperty, Long> getEntityDao() {
        return configPropertyDao;
    }

    @Transactional(readOnly = true)
    public Optional<ConfigProperty> findByPropKey(String propKey) {
        return configPropertyDao.findByPropKey(propKey);
    }

    @Transactional(readOnly = true)
    public String findValueByPropKey(String propKey) {
        return configPropertyDao.findByPropKey(propKey).map(one -> one.getSimpleValue()).orElse(null);
    }

    @Transactional(readOnly = true)
    public String findHtmlByPropKey(String propKey) {
        return configPropertyDao.findByPropKey(propKey).map(one -> one.getHtmlValue()).orElse(null);
    }
}
