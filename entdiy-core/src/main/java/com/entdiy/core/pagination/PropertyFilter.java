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

import com.entdiy.core.util.reflection.ConvertUtils;
import com.entdiy.core.web.json.JsonViews;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 与具体ORM实现无关的属性过滤条件封装类, 主要记录页面中简单的搜索过滤条件. 用于页面表单传入字符串形式条件，然后转换处理为DAO层面识别的SQL条件
 * 页面表单元素示例：
 * <ul>
 * <li>search[CN_a_OR_b]</li>
 * <li>search[EQ_id]</li>
 * <li>search[CN_user.name]</li>
 * </ul>
 * <p>
 * FORM传递表单参数规则： <br/>
 * 1, 第一部分：以"search[]"作为查询参数标识 <br/>
 * 2, 第二部分：查询类型，@see #MatchType <br/>
 * 3, 第三部分：id_OR_email，category，state, user.userprofile为属性名称,一般对应于Hibernate
 * Entity对应属性,可以以_OR_分隔多个属性进行OR查询
 * </p>
 * <p>
 * 上述拼装字符串形式主要用于JSP页面form表单元素name属性值,如果是Java代码层面追加过滤条件,一般直接用构造函数:
 * PropertyFilter(final MatchType matchType, final String propertyName, final
 * Object matchValue)
 * </p>
 */
public class PropertyFilter {

    private final static Logger logger = LoggerFactory.getLogger(PropertyFilter.class);

    private static final int DEFAULT_PAGE_ROWS = 20;

    private static final String SEARCH_MAP_PREFIX = "search";

    /**
     * 多个属性间OR关系的分隔符.
     */
    public static final String OR_SEPARATOR = "_OR_";

    /**
     * 属性匹配比较类型.
     */
    public enum MatchType {
        /**
         * "name": "bk", "description": "is blank", "operator":"IS NULL OR ==''"
         */
        BK,

        /**
         * "name": "nb", "description": "is not blank", "operator":"IS NOT NULL AND !=''"
         */
        NB,

        /**
         * "name": "nu", "description": "is null", "operator":"IS NULL"
         */
        NU,

        /**
         * "name": "nn", "description": "is not null", "operator":"IS NOT NULL"
         */
        NN,

        /**
         * "name": "in", "description": "in", "operator":"IN"
         */
        IN,

        /**
         * "name": "ni", "description": "not in", "operator":"NOT IN"
         */
        NI,

        /**
         * "name": "ne", "description": "not equal", "operator":"<>"
         */
        NE,

        /**
         * "name": "eq", "description": "equal", "operator":"="
         */
        EQ,

        /**
         * "name": "cn", "description": "contains", "operator":"LIKE %abc%"
         */
        CN,

        /**
         * "name": "nc", "description": "does not contain",
         * "operator":"NOT LIKE %abc%"
         */
        NC,

        /**
         * "name": "bw", "description": "begins with", "operator":"LIKE abc%"
         */
        BW,

        /**
         * "name": "bn", "description": "does not begin with",
         * "operator":"NOT LIKE abc%"
         */
        BN,

        /**
         * "name": "ew", "description": "ends with", "operator":"LIKE %abc"
         */
        EW,

        /**
         * "name": "en", "description": "does not end with",
         * "operator":"NOT LIKE %abc"
         */
        EN,

        /**
         * "name": "bt", "description": "between", "operator":"BETWEEN 1 AND 2"
         */
        BT,

        /**
         * "name": "lt", "description": "less", "operator":"小于"
         */
        LT,

        /**
         * "name": "gt", "description": "greater", "operator":"大于"
         */
        GT,

        /**
         * "name": "le", "description": "less or equal","operator":"<="
         */
        LE,

        /**
         * "name": "ge", "description": "greater or equal", "operator":">="
         */
        GE,

        /**
         * INNER|LEFT|RIGHT
         *
         * @see javax.persistence.criteria.Fetch
         */
        FETCH,

        /**
         * Property Less Equal: <=
         */
        PLE,

        /**
         * Property Less Than: <
         */
        PLT,

        ACLPREFIXS
    }

    /**
     * 原始过滤字符串表达式
     */
    private String filterName;

    /**
     * 匹配类型
     */
    private MatchType matchType = null;

    /**
     * 匹配值
     */
    private Object matchValue = null;

    /**
     * 匹配属性类型
     * 限制说明：如果是多个属性则取第一个,因此需要确保_OR_定义多个属性属于同一类型
     */

    private Class propertyClass = null;

    /**
     * 属性名称数组, 一般是单个属性,如果有_OR_则为多个
     */
    private String[] propertyNames = null;
    /**
     * 集合类型子查询,如查询包含某个商品的所有订单列表,如order上面有个List集合products对象，则可以类似这样:search['EQ_products.code']
     * 限制说明：框架只支持当前主对象直接定义的集合对象集合查询，不支持再多层嵌套
     */
    private Class subQueryCollectionPropetyType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyFilter that = (PropertyFilter) o;

        if (!filterName.equals(that.filterName)) return false;
        return propertyClass.equals(that.propertyClass);
    }

    @Override
    public int hashCode() {
        int result = filterName.hashCode();
        result = 31 * result + propertyClass.hashCode();
        return result;
    }

    /**
     * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表.
     * @param values     待比较的值.
     */
    public PropertyFilter(Class<?> entityClass, String filterName, String... values) {
        this.filterName = filterName;
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        if (matchTypeCode.indexOf("@") > -1) {
            String[] matchTypeCodes = matchTypeCode.split("@");
            matchTypeCode = matchTypeCodes[0];

            String propertyType = matchTypeCodes[1];
            if (Date.class.getName().equalsIgnoreCase(propertyType)) {
                propertyClass = Date.class;
            } else if (Number.class.getName().equalsIgnoreCase(propertyType)) {
                propertyClass = Number.class;
            } else {
                propertyClass = String.class;
            }
        }

        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
        }

        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        Assert.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
        propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, PropertyFilter.OR_SEPARATOR);
        //计算属性对应Class类型
        if (propertyNameStr.indexOf("count(") > -1) {
            propertyClass = Integer.class;
        } else if (propertyNameStr.indexOf("(") > -1) {
            propertyClass = BigDecimal.class;
        } else {
            String firstPropertyName = propertyNames[0];
            if (firstPropertyName.indexOf("@") > -1) {
                String propertyType = firstPropertyName.split("@")[1];
                if (LocalDate.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = LocalDate.class;
                }else if (Date.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = LocalDate.class;
                } else if (LocalDateTime.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = LocalDateTime.class;
                } else if (Number.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = Number.class;
                } else if (Boolean.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = Boolean.class;
                } else if (String.class.getSimpleName().equalsIgnoreCase(propertyType)) {
                    propertyClass = String.class;
                }
            } else {
                Method method = null;
                String[] namesSplits = StringUtils.split(firstPropertyName, ".");
                if (namesSplits.length == 1) {
                    method = MethodUtils.getAccessibleMethod(entityClass, "get" + StringUtils.capitalize(propertyNames[0]));
                } else {
                    Class<?> retClass = entityClass;
                    for (String nameSplit : namesSplits) {
                        method = MethodUtils.getAccessibleMethod(retClass, "get" + StringUtils.capitalize(nameSplit));
                        retClass = method.getReturnType();
                        if (Collection.class.isAssignableFrom(retClass)) {
                            Type genericReturnType = method.getGenericReturnType();
                            if (genericReturnType instanceof ParameterizedType) {
                                retClass = (Class<?>) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                                subQueryCollectionPropetyType = retClass;
                            }
                        }
                    }
                }
                if (method == null) {
                    propertyClass = String.class;
                } else {
                    propertyClass = method.getReturnType();
                }
            }
        }

        if (values.length == 1) {
            if (matchType.equals(MatchType.IN) || matchType.equals(MatchType.NI)) {
                String value = values[0];
                values = value.split(",");
            } else if (propertyClass.equals(LocalDate.class)) {
                String value = values[0].trim();
                value = value.replace("～", "~");
                if (value.indexOf(" ") > -1) {
                    values = StringUtils.split(value, "~");
                    if (matchType.equals(MatchType.BT)) {
                        values[0] = values[0].trim();
                        values[1] = values[1].trim();
                    } else {
                        values = new String[]{values[0].trim()};
                    }
                }
            }
        }

        if (values.length == 1) {
            this.matchValue = parseMatchValueByClassType(propertyClass, values[0]);
        } else {
            Object[] matchValues = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                matchValues[i] = parseMatchValueByClassType(propertyClass, values[i]);
            }
            this.matchValue = matchValues;
        }
    }

    private Object parseMatchValueByClassType(Class propertyClass, String value) {
        if ("NULL".equalsIgnoreCase(value)) {
            return value;
        }
        if (Enum.class.isAssignableFrom(propertyClass)) {
            return Enum.valueOf(propertyClass, value);
        } else if (propertyClass.equals(Boolean.class) || matchType.equals(MatchType.NN) || matchType.equals(MatchType.NU)) {
            return new Boolean(BooleanUtils.toBoolean(value));
        } else if (propertyClass.equals(LocalDate.class)) {
            return LocalDate.parse(value);
        } else if (propertyClass.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(value);
        } else if (propertyClass.equals(Number.class)) {
            if (value.indexOf(".") > -1) {
                return new Double(value);
            } else {
                return new Long(value);
            }
        } else {
            return ConvertUtils.convertStringToObject(value, propertyClass);
        }
    }

    /**
     * Java程序层直接构造过滤器对象, 如filters.add(new PropertyFilter(MatchType.EQ, "code",
     * code));
     *
     * @param matchType
     * @param propertyName
     * @param matchValue
     */
    public PropertyFilter(final MatchType matchType, final String propertyName, final Object matchValue) {
        this.matchType = matchType;
        this.propertyNames = StringUtils.splitByWholeSeparator(propertyName, PropertyFilter.OR_SEPARATOR);
        this.matchValue = matchValue;
    }

    /**
     * Java程序层直接构造过滤器对象, 如filters.add(new PropertyFilter(MatchType.LIKE, new
     * String[]{"code","name"}, value));
     *
     * @param matchType
     * @param propertyName
     * @param matchValue
     */
    public PropertyFilter(final MatchType matchType, final String[] propertyNames, final Object matchValue) {
        this.matchType = matchType;
        this.propertyNames = propertyNames;
        this.matchValue = matchValue;
    }

    /**
     * 从HttpRequest中创建PropertyFilter列表
     * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
     */
    public static List<PropertyFilter> buildFiltersFromHttpRequest(Class<?> entityClass, ServletRequest request) {

        List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

        // 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
        Map<String, String[]> filterParamMap = getParametersStartingWith(request, SEARCH_MAP_PREFIX + "[", "]");

        // 分析参数Map,构造PropertyFilter列表
        for (Map.Entry<String, String[]> entry : filterParamMap.entrySet()) {
            String filterName = entry.getKey();
            String[] values = entry.getValue();
            if (values == null || values.length == 0) {
                continue;
            }

            if (values.length == 1) {
                String value = values[0];
                // 如果value值为空,则忽略此filter.
                if (StringUtils.isNotBlank(value)) {
                    PropertyFilter filter = new PropertyFilter(entityClass, filterName, value);
                    filterList.add(filter);
                }
            } else {
                String[] valuesArr = values;
                // 如果value值为空,则忽略此filter.
                if (valuesArr.length > 0) {
                    Set<String> valueSet = new HashSet<String>();
                    for (String value : valuesArr) {
                        if (StringUtils.isNotBlank(value)) {
                            valueSet.add(value);
                        }
                    }
                    if (valueSet.size() > 0) {
                        String[] realValues = new String[valueSet.size()];
                        int cnt = 0;
                        for (String v : valueSet) {
                            realValues[cnt++] = v;
                        }
                        PropertyFilter filter = new PropertyFilter(entityClass, filterName, realValues);
                        filterList.add(filter);
                    }

                }
            }

        }
        return filterList;
    }

    /**
     * 从request对象中提取组装分页和排序对象，参数列表：
     * rows 每页记录数，如果值为空则取rows参数值；如果值为负数则返回null表示不分页
     * page 当前页码数，从1开始，默认为1
     * start 起始记录顺序号，从1开始，用于一些需要精确控制从start到start+size的场景；可选参数，未提供则取值等于page*size
     * sidx 排序属性名称
     * sord 排序规则，asc=升序，desc=降序，默认asc
     *
     * @param request     HttpServletRequest对象
     * @param defaultRows 如果request中未提供rows参数时默认记录数
     * @return
     */
    public static Pageable buildPageableFromHttpRequest(HttpServletRequest request, int defaultRows) {
        return buildPageableFromHttpRequest(request, null, defaultRows);
    }

    /**
     * 从request对象中提取组装分页和排序对象，参数列表：
     * rows 每页记录数，默认20
     * page 当前页码数，从1开始，默认为1
     * start 起始记录顺序号，从1开始，用于一些需要精确控制从start到start+size的场景；可选参数，未提供则取值等于page*size
     * sidx 排序属性名称
     * sord 排序规则，asc=升序，desc=降序，默认asc
     *
     * @param request HttpServletRequest对象
     * @return
     */
    public static Pageable buildPageableFromHttpRequest(HttpServletRequest request) {
        return buildPageableFromHttpRequest(request, null, DEFAULT_PAGE_ROWS);
    }

    /**
     * 从request对象中提取组装分页和排序对象，参数列表：
     * rows 每页记录数，如果值为空则取rows参数值；如果值为负数则返回null表示不分页
     * page 当前页码数，从1开始，默认为1
     * start 起始记录顺序号，从1开始，用于一些需要精确控制从start到start+size的场景；可选参数，未提供则取值等于page*size
     * sidx 排序属性名称
     * sord 排序规则，asc=升序，desc=降序，默认asc
     *
     * @param request
     * @param sort        如果传入参数为null，则从request构建，否则直接取输入sort参数
     * @param defaultRows 如果request中未提供rows参数时默认记录数
     * @return
     */
    public static Pageable buildPageableFromHttpRequest(HttpServletRequest request, Sort sort, int defaultRows) {
        int rows = defaultRows;
        String paramRows = request.getParameter("rows");
        if (StringUtils.isNotBlank(paramRows)) {
            int pr = Integer.valueOf(paramRows);
            if (pr <= 0) {
                return null;
            } else {
                rows = pr;
            }
        }
        int offset = -1;
        int page = 1;
        String strStart = request.getParameter("start");
        if (StringUtils.isNotBlank(strStart)) {
            offset = Integer.valueOf(strStart) - 1;
            page = (offset + 1) / Integer.valueOf(rows);
            if (page <= 0) {
                page = 1;
            }
        } else {
            String strPage = request.getParameter("page");
            if (StringUtils.isNotBlank(strPage)) {
                page = Integer.valueOf(strPage);
            }
        }

        if (sort == null) {
            sort = buildSortFromHttpRequest(request);
        }
        return new ExtPageRequest(offset, page - 1, Integer.valueOf(rows), sort);
    }

    /**
     * 从request对象中提取组装分页和排序对象，参数列表：
     * rows 每页记录数，默认20
     * page 当前页码数，从1开始，默认为1
     * start 起始记录顺序号，从1开始，用于一些需要精确控制从start到start+size的场景；可选参数，未提供则取值等于page*size
     * sidx 排序属性名称
     * sord 排序规则，asc=升序，desc=降序，默认asc
     *
     * @param request
     * @param sort        如果传入参数为null，则从request构建，否则直接取输入sort参数
     * @return
     */
    public static Pageable buildPageableFromHttpRequest(HttpServletRequest request, Sort sort) {
        return buildPageableFromHttpRequest(request, sort, DEFAULT_PAGE_ROWS);
    }

    /**
     * 从request对象中提取组装排序对象，参数列表：
     * sidx 排序属性名称
     * sord 排序规则，asc=升序，desc=降序，默认asc
     *
     * @param request
     * @return
     */
    public static Sort buildSortFromHttpRequest(HttpServletRequest request) {
        String sidx = StringUtils.isBlank(request.getParameter("sidx")) ? "id" : request.getParameter("sidx");
        Direction sord = "desc".equalsIgnoreCase(request.getParameter("sord")) ? Direction.DESC : Direction.ASC;
        Sort sort = null;

        //按照逗号切分支持多属性排序
        for (String sidxItem : sidx.split(",")) {
            if (StringUtils.isNotBlank(sidxItem)) {
                //再按空格切分获取排序属性和排序方向
                String[] sidxItemWithOrder = sidxItem.trim().split(" ");
                String sortname = sidxItemWithOrder[0];
                //如果查询属性包含_OR_则取第一个作为排序属性
                //因此在写OR多属性查询时注意把排序属性写在最前面
                if (sortname.indexOf(OR_SEPARATOR) > -1) {
                    sortname = StringUtils.substringBefore(sortname, OR_SEPARATOR);
                }

                //去除类型标识信息
                sortname = sortname.split("@")[0];

                //如果单个属性没有跟随排序方向，则取Grid组件传入的sord参数定义
                if (sidxItemWithOrder.length == 1) {
                    if (sort == null) {
                        //初始化排序对象
                        sort = new Sort(sord, sortname);
                    } else {
                        //and追加多个排序
                        sort = sort.and(new Sort(sord, sortname));
                    }
                } else {
                    //排序属性后面空格跟随排序方向定义
                    String sortorder = sidxItemWithOrder[1];
                    if (sort == null) {
                        sort = new Sort("desc".equalsIgnoreCase(sortorder) ? Direction.DESC : Direction.ASC, sortname);
                    } else {
                        sort = sort.and(new Sort("desc".equalsIgnoreCase(sortorder) ? Direction.DESC : Direction.ASC, sortname));
                    }
                }
            }
        }
        return sort;
    }

    /**
     * 获取比较方式.
     */
    public MatchType getMatchType() {
        return matchType;
    }

    /**
     * 获取比较值.
     */
    public Object getMatchValue() {
        return matchValue;
    }

    /**
     * 获取比较属性名称列表.
     */
    public String[] getConvertedPropertyNames() {
        String[] convertedPropertyNames = new String[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            convertedPropertyNames[i] = propertyNames[i].split("@")[0];
        }
        return convertedPropertyNames;
    }

    /**
     * 获取唯一的比较属性名称.
     */
    public String getConvertedPropertyName() {
        Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
        String propertyName = propertyNames[0];
        //移除@后面的类型标识信息
        return propertyName.split("@")[0];
    }

    /**
     * 是否比较多个属性.
     */
    public boolean hasMultiProperties() {
        return (propertyNames.length > 1);
    }

    /**
     * 构造一个缺省过滤集合.
     */
    public static List<PropertyFilter> buildDefaultFilterList() {
        return new ArrayList<PropertyFilter>();
    }

    public Class getPropertyClass() {
        return propertyClass;
    }

    public Class getSubQueryCollectionPropetyType() {
        return subQueryCollectionPropetyType;
    }

    /**
     * 解析请求参数动态返回Jackson渲染视图定义
     *
     * @param request
     * @return
     */
    public static Class<? extends JsonViews.View> parseJsonView(ServletRequest request, Class<? extends JsonViews.View> defaultView) {
        String view = request.getParameter("_view");
        if (StringUtils.isNotBlank(view)) {
            if (view.equalsIgnoreCase("tree")) {
                return JsonViews.Tree.class;
            }
        }
        return defaultView;
    }

    private static Map<String, String[]> getParametersStartingWith(ServletRequest request, String prefix, String suffix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, String[]> params = Maps.newTreeMap();
        if (prefix == null) {
            prefix = "";
        }
        if (suffix == null) {
            suffix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (("".equals(prefix) || paramName.startsWith(prefix)) && ("".equals(suffix) || paramName.endsWith(suffix))) {
                String unprefixed = paramName.substring(prefix.length(), paramName.length() - suffix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, new String[]{values[0]});
                }
            }
        }
        return params;
    }
}
