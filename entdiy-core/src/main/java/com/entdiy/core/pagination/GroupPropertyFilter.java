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
package com.entdiy.core.pagination;

import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于jqGrid自定义高级查询条件封装条件组合
 */
public class GroupPropertyFilter {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /** 组合类型:AND/OR */
    @Getter
    @Setter
    private GroupOperationEnum groupType = GroupOperationEnum.AND;

    /** 组合条件列表 */
    @Getter
    @Setter
    private List<PropertyFilter> filters = Lists.newArrayList();

    /** 强制追加AND条件列表 */
    private List<PropertyFilter> forceAndFilters = Lists.newArrayList();

    /** 组合条件组 */
    @Getter
    @Setter
    private List<GroupPropertyFilter> groups = Lists.newArrayList();

    @Getter
    @Setter
    private Class<?> entityClass;


    public List<PropertyFilter> getForceAndFilters() {
        return forceAndFilters;
    }

    private GroupPropertyFilter() {
    }

    public GroupPropertyFilter append(GroupPropertyFilter... groups) {
        this.groups.addAll(Lists.newArrayList(groups));
        return this;
    }

    public GroupPropertyFilter append(PropertyFilter... filters) {
        this.filters.addAll(Lists.newArrayList(filters));
        return this;
    }

    public GroupPropertyFilter forceAnd(PropertyFilter... filters) {
        this.forceAndFilters.addAll(Lists.newArrayList(filters));
        return this;
    }

    public static GroupPropertyFilter buildFromHttpRequest(Class<?> entityClass, HttpServletRequest request) {

        try {
            String filtersJson = request.getParameter("filters");

            GroupPropertyFilter groupPropertyFilter = new GroupPropertyFilter();
            groupPropertyFilter.setEntityClass(entityClass);

            if (StringUtils.isNotBlank(filtersJson)) {
                JqGridSearchFilter jqFilter = objectMapper.readValue(filtersJson, JqGridSearchFilter.class);
                convertJqGridToFilter(entityClass, groupPropertyFilter, jqFilter);
            }

            List<PropertyFilter> filters = PropertyFilter.buildFiltersFromHttpRequest(entityClass, request);
            if (CollectionUtils.isNotEmpty(filters)) {

                GroupPropertyFilter comboGroupPropertyFilter = new GroupPropertyFilter();
                comboGroupPropertyFilter.setEntityClass(entityClass);
                comboGroupPropertyFilter.setGroupType(GroupOperationEnum.AND);

                GroupPropertyFilter normalGroupPropertyFilter = new GroupPropertyFilter();
                normalGroupPropertyFilter.setGroupType(GroupOperationEnum.AND);
                normalGroupPropertyFilter.setFilters(filters);
                comboGroupPropertyFilter.getGroups().add(normalGroupPropertyFilter);

                comboGroupPropertyFilter.getGroups().add(groupPropertyFilter);
                return comboGroupPropertyFilter;
            }

            return groupPropertyFilter;

        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public static GroupPropertyFilter buildDefaultAndGroupFilter(PropertyFilter... filters) {
        GroupPropertyFilter dpf = new GroupPropertyFilter();
        dpf.setGroupType(GroupOperationEnum.AND);
        if (filters != null && filters.length > 0) {
            dpf.setFilters(Lists.newArrayList(filters));
        }
        return dpf;
    }

    public static GroupPropertyFilter buildDefaultOrGroupFilter(PropertyFilter... filters) {
        GroupPropertyFilter dpf = new GroupPropertyFilter();
        dpf.setGroupType(GroupOperationEnum.OR);
        if (filters != null && filters.length > 0) {
            dpf.setFilters(Lists.newArrayList(filters));
        }
        return dpf;
    }

    private static void convertJqGridToFilter(Class<?> entityClass, GroupPropertyFilter jqGroupPropertyFilter, JqGridSearchFilter jqFilter) {
        jqGroupPropertyFilter.setGroupType("OR".equalsIgnoreCase(jqFilter.getGroupOp()) ? GroupOperationEnum.OR : GroupOperationEnum.AND);

        List<JqGridSearchRule> rules = jqFilter.getRules();
        List<PropertyFilter> filters = Lists.newArrayList();
        for (JqGridSearchRule rule : rules) {
            if (StringUtils.isBlank(rule.getData())) {
                continue;
            }
            PropertyFilter filter = new PropertyFilter(entityClass, rule.getOp().toUpperCase() + "_" + rule.getField(), rule.getData());
            filters.add(filter);
        }
        jqGroupPropertyFilter.setFilters(filters);

        List<JqGridSearchFilter> groups = jqFilter.getGroups();
        for (JqGridSearchFilter group : groups) {
            GroupPropertyFilter jqChildGroupPropertyFilter = new GroupPropertyFilter();
            jqGroupPropertyFilter.getGroups().add(jqChildGroupPropertyFilter);
            convertJqGridToFilter(entityClass, jqChildGroupPropertyFilter, group);
        }
    }

    /**
     * filters =
     * {"groupOp":"AND","rules":[{"field":"code","op":"eq","data":"123"}]}
     */
    @Getter
    @Setter
    public static class JqGridSearchFilter {
        private String groupOp;
        private List<JqGridSearchRule> rules = Lists.newArrayList();
        private List<JqGridSearchFilter> groups = Lists.newArrayList();
    }

    @Getter
    @Setter
    public static class JqGridSearchRule {
        /** 查询字段 */
        private String field;

        /**
         * 查询匹配规则
         * [
         * 'eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'
         * ] The corresponding texts are in language file and mean the
         * following: ['equal','not equal', 'less', 'less or
         * equal','greater','greater or equal', 'begins with','does not begin
         * with','is in','is not in','ends with','does not end
         * with','contains','does not contain']
         */
        private String op;

        /** 查询数据 */
        private String data;
    }

    /**
     * 判断当前是没有提供任何参数的默认查询
     * 一般用于父子结构类型数据根据无参数判断追加parent==null的查询条件
     *
     * @return
     */
    public boolean isEmptySearch() {
        if (CollectionUtils.isEmpty(filters) && CollectionUtils.isEmpty(groups)) {
            return true;
        }
        Set<PropertyFilter> mergeFileters = Sets.newHashSet();
        mergeFileters.addAll(filters);
        for (GroupPropertyFilter group : groups) {
            mergeFileters.addAll(group.getFilters());
            mergeFileters.addAll(group.getForceAndFilters());
        }
        for (PropertyFilter filter : mergeFileters) {
            //FETCH类型不算有效的查询条件，忽略掉
            if (!filter.getMatchType().equals(MatchType.FETCH)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换为Key-Value的Map结构数据
     *
     * @return
     */
    public Map<String, Object> convertToMapParameters() {
        Map<String, Object> parameters = Maps.newHashMap();
        List<PropertyFilter> propertyFilters = Lists.newArrayList();
        propertyFilters.addAll(this.getFilters());
        if (CollectionUtils.isNotEmpty(this.getGroups())) {
            for (GroupPropertyFilter group : this.getGroups()) {
                if (CollectionUtils.isEmpty(group.getFilters()) && CollectionUtils.isEmpty(group.getForceAndFilters())) {
                    continue;
                }
                propertyFilters.addAll(group.getFilters());
            }
        }
        for (PropertyFilter propertyFilter : propertyFilters) {
            String name = propertyFilter.getConvertedPropertyName();
            //把.分隔转换为_形式，以便在MyBatis中引用
            name = name.replaceAll("\\.", "_");
            parameters.put(name, propertyFilter.getMatchValue());
        }
        return parameters;
    }

    /**
     * 转换为PropertyFilter查询集合，用于传递给MyBatis作为动态查询参数
     * 注意限制：仅考虑最常用的一个层次的查询调整合并处理，暂不支持复杂的多级嵌套层次查询
     *
     * @return
     */
    public List<PropertyFilter> convertToPropertyFilters() {
        List<PropertyFilter> propertyFilters = Lists.newArrayList();
        propertyFilters.addAll(this.getFilters());
        if (CollectionUtils.isNotEmpty(this.getGroups())) {
            for (GroupPropertyFilter group : this.getGroups()) {
                if (CollectionUtils.isEmpty(group.getFilters()) && CollectionUtils.isEmpty(group.getForceAndFilters())) {
                    continue;
                }
                propertyFilters.addAll(group.getFilters());
            }
        }
        return propertyFilters;
    }

    public enum GroupOperationEnum {
        AND, OR
    }
}
