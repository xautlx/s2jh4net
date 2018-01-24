/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.entdiy.core.pagination.GroupPropertyFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface MyBatisDao {

    <E> List<E> findList(String namespace, String statementId, Map<String, Object> parameters);

    <E> List<E> findLimitList(String namespace, String statementId, Map<String, Object> parameters, Integer top);

    <E> List<E> findSortList(String namespace, String statementId, GroupPropertyFilter groupPropertyFilter, Sort sort);

    <E> List<E> findSortList(String namespace, String statementId, Map<String, Object> parameters, Sort sort);

    <E> Page<E> findPage(String namespace, String statementId, Map<String, Object> parameters, Pageable pageable);

    <E> Page<E> findPage(String namespace, String statementId, GroupPropertyFilter groupPropertyFilter, Pageable pageable);

    <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameters, String mapKey);

    <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameter, String mapKey, Integer top);

    /**
     * 执行更新操作，包括insert、update、delete
     * @param namespace 一般用实体类的全路径,如 User.class.getName()
     * @param statementId insert、update、delete等语句标识
     * @param parameters 操作所需参数对象，可能是实体对象或Map或其他类型等
     * @return 注意: 返回的是操作影响的记录数,不是主键
     */
    //由于本框架优先采用Hibernate进行数据管理，因此MyBatis仅用于复杂的查询之用，不做数据更新操作以免干扰Hibernate缓存
    //int execute(String namespace, String statementId, Object parameters);
}
