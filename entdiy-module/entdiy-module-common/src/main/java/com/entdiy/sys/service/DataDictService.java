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

import com.entdiy.core.service.BaseNestedSetService;
import com.entdiy.sys.dao.DataDictDao;
import com.entdiy.sys.entity.DataDict;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DataDictService extends BaseNestedSetService<DataDict, Long> {

    private static final Logger logger = LoggerFactory.getLogger(DataDictService.class);

    @Autowired
    private DataDictDao dataDictDao;

    @Transactional(readOnly = true)
    public List<DataDict> findAllCached() {
        return dataDictDao.findAllCached();
    }

    public Optional<DataDict> findByRootPrimaryKey(String primaryKey) {
        return dataDictDao.findByRootPrimaryKey(primaryKey);
    }

    /**
     * 基于主键返回对应的数据字典子集合
     *
     * @param id 主键
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenById(Long id) {
        return findChildrenById(id, false);
    }

    /**
     * 基于主键返回对应的数据字典子集合
     *
     * @param id               主键
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenById(Long id, boolean withFlatChildren) {
        return findChildrens(findOne(id), withFlatChildren);
    }

    /**
     * 直接基于根节点primaryKey返回对应的数据字典集合
     *
     * @param primaryKey 根节点primaryKey
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenByRootPrimaryKey(String primaryKey) {
        return findChildrenByRootPrimaryKey(primaryKey, false);
    }

    /**
     * 直接基于根节点primaryKey返回对应的数据字典集合
     *
     * @param primaryKey       根节点primaryKey
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenByRootPrimaryKey(String primaryKey, boolean withFlatChildren) {
        return findChildrens(dataDictDao.findByRootPrimaryKey(primaryKey), withFlatChildren);
    }

    private List<DataDict> findChildrens(Optional<DataDict> parent, boolean withFlatChildren) {
        List<DataDict> dataDicts = Lists.newArrayList();
        parent.ifPresent(one -> {
            List<DataDict> roots = dataDictDao.findEnabledChildrenByParentId(one.getId());
            dataDicts.addAll(roots);
            if (withFlatChildren) {
                for (DataDict dataDict : roots) {
                    List<DataDict> chidren = dataDictDao.findEnabledChildrenByParentId(dataDict.getId());
                    dataDicts.addAll(chidren);
                }
            }
        });
        return dataDicts;
    }

    /**
     * 直接基于根节点primaryKey返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     *
     * @param primaryKey 根节点primaryKey
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataByRootPrimaryKey(String primaryKey) {
        return findMapDataByRootPrimaryKey(primaryKey, false);
    }

    /**
     * 基于主键返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     *
     * @param id 主键
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataById(Long id) {
        return findMapDatas(findOne(id), false);
    }

    /**
     * 直接基于根节点primaryKey返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     *
     * @param primaryKey       根节点primaryKey
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataByRootPrimaryKey(String primaryKey, boolean withFlatChildren) {
        return findMapDatas(dataDictDao.findByRootPrimaryKey(primaryKey), withFlatChildren);
    }

    private Map<String, String> findMapDatas(Optional<DataDict> parent, boolean withFlatChildren) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrens(parent, withFlatChildren);
        if (dataDicts != null) {
            for (DataDict dataDict : dataDicts) {
                dataMap.put(dataDict.getPrimaryKey(), dataDict.getPrimaryValue());
            }
        }
        return dataMap;
    }

    @Transactional(readOnly = true)
    public Map<String, DataDict> findMapObjectByRootPrimaryKey(String primaryKey) {
        Map<String, DataDict> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrenByRootPrimaryKey(primaryKey);
        if (dataDicts != null) {
            for (DataDict dataDict : dataDicts) {
                dataMap.put(dataDict.getPrimaryKey(), dataDict);
            }
        } else {
            logger.warn("Undefined DataDict for primaryKey: {}", primaryKey);
        }
        return dataMap;
    }
}
