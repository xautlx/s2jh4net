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
package com.entdiy.core.service;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.entity.BaseNativeNestedSetEntity;
import com.entdiy.core.pagination.ExtPageRequest;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.Case;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseService<T extends AbstractPersistableEntity, ID extends Serializable> {

    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected Class<T> entityClass;

    protected String entityName;

    protected SimpleJpaRepository<T, ID> jpaRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @PostConstruct
    public void init() {
        // 通过反射取得Entity的Class.
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        //获取实体类对应的JQL实体名称
        Entity annoEntity = entityClass.getAnnotation(Entity.class);
        this.entityName = annoEntity.name();
        if (StringUtils.isBlank(entityName)) {
            this.entityName = entityClass.getSimpleName();
        }

        //构造对应的JPA操作接口对象
        this.jpaRepository = new SimpleJpaRepository(entityClass, entityManager);
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    @Transactional
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Transactional
    public Iterable<T> saveAll(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<T> result = new ArrayList();
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    /**
     * 基于主键查询单一数据对象
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        return jpaRepository.findById(id).orElse(null);
    }

    /**
     * 基于主键查询单一数据对象
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<T> findOptionalOne(ID id) {
        return jpaRepository.findById(id);
    }

    /**
     * 基于主键集合查询集合数据对象，如果可变参数为空则查询全部数据
     *
     * @param ids 主键集合
     * @return never {@literal null}.
     */
    @Transactional(readOnly = true)
    public List<T> findAll(final ID... ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return jpaRepository.findAll();
        } else {
            return jpaRepository.findAll((root, query, builder) -> root.get("id").in(ids));
        }
    }

    /**
     * 数据删除操作
     *
     * @param entity 待操作数据
     */
    @Transactional
    public void delete(T entity) {
        jpaRepository.delete(entity);
    }

    /**
     * 批量数据删除操作 其实现只是简单循环集合每个元素调用 {@link #delete(AbstractPersistableEntity)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     *
     * @param entities 待批量操作数据集合
     * @return
     */
    @Transactional
    public void deleteAll(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * 单一条件对象查询数据集合
     *
     * @param propertyFilter
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilter(PropertyFilter propertyFilter) {
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultAndGroupFilter(propertyFilter);
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return jpaRepository.findAll(spec);
    }

    /**
     * 基于查询条件count记录数据
     *
     * @param groupPropertyFilter
     * @return
     */
    @Transactional(readOnly = true)
    public long count(GroupPropertyFilter groupPropertyFilter) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return jpaRepository.count(spec);
    }


    /**
     * count总记录数据
     *
     * @return
     */
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }

    /**
     * 基于动态组合条件对象查询数据集合
     *
     * @param groupPropertyFilter
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return jpaRepository.findAll(spec);
    }

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     *
     * @param groupPropertyFilter
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter, Sort sort) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return jpaRepository.findAll(spec, sort);
    }

    @Transactional(readOnly = true)
    public <X extends Persistable> List<X> findByFilters(Class<X> clazz, GroupPropertyFilter groupPropertyFilter, Sort sort) {
        Specification<X> spec = buildSpecification(groupPropertyFilter);
        return ((BaseDao) spec).findAll(spec, sort);
    }

    /**
     * 基于动态组合条件对象和排序定义，限制查询数查询数据集合
     * 主要用于Autocomplete这样的查询避免返回太多数据
     *
     * @param groupPropertyFilter
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter, Sort sort, int limit) {
        Pageable pageable = PageRequest.of(0, limit, sort);
        return findByPage(groupPropertyFilter, pageable).getContent();
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     *
     * @param groupPropertyFilter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public JsonPage<T> findByPage(GroupPropertyFilter groupPropertyFilter, Pageable pageable) {
        Specification<T> specifications = buildSpecification(groupPropertyFilter);
        return new JsonPage(jpaRepository.findAll(specifications, pageable));
    }

    @Getter
    @Setter
    private class GroupAggregateProperty {
        @MetaData(value = "字面属性", comments = "最后用于前端JSON输出的key")
        private String label;

        @MetaData(value = "JPA表达式", comments = "传入JPA CriteriaBuilder组装的内容")
        private String name;

        @MetaData(value = "JPA表达式alias", comments = "用于获取聚合值的别名")
        private String alias;

        @MetaData(value = "JPA Expression")
        private Expression expression;
    }

    /**
     * 分组聚合统计，常用于类似按账期时间段统计商品销售利润，按会计科目总帐统计等
     *
     * @param groupFilter 过滤参数对象
     * @param pageable    分页排序参数对象，可为null表示不做分页查询。TODO：目前有个限制未实现总记录数处理，直接返回一个固定大数字
     * @param properties  属性集合，判断规则：属性名称包含"("则标识为聚合属性，其余为分组属性
     *                    属性语法规则：sum = + , diff = - , prod = * , quot = / , case(condition,when,else)
     *                    示例：
     *                    sum(amount) as xyz
     *                    count(id) as xyz
     *                    sum(diff(amount,costAmount)) as xyz
     *                    min(case(equal(amount,0),-1,quot(diff(amount,costAmount),amount))) as xyz
     *                    case(equal(sum(amount),0),-1,quot(sum(diff(amount,costAmount)),sum(amount))) as xyz
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> findByGroupAggregate(GroupPropertyFilter groupFilter, Pageable pageable, String... properties) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<?> root = criteriaQuery.from(groupFilter.getEntityClass());

        //挑出分组和聚合属性组，以是否存在“(”作为标识
        List<GroupAggregateProperty> groupProperties = Lists.newArrayList();
        List<GroupAggregateProperty> aggregateProperties = Lists.newArrayList();
        for (String prop : properties) {
            GroupAggregateProperty groupAggregateProperty = new GroupAggregateProperty();
            //聚合类型表达式
            if (prop.indexOf("(") > -1) {
                //处理as别名
                prop = prop.replace(" AS ", " as ").replace(" As ", " as ").replace(" aS ", " as ");
                String[] splits = prop.split(" as ");
                String alias = null;
                String name = null;
                if (splits.length > 1) {
                    name = splits[0].trim();
                    alias = splits[1].trim();
                    groupAggregateProperty.setAlias(alias);
                    groupAggregateProperty.setLabel(alias);
                    groupAggregateProperty.setName(name);
                } else {
                    name = splits[0].trim();
                    alias = fixCleanAlias(name);
                    groupAggregateProperty.setAlias(alias);
                    groupAggregateProperty.setLabel(name);
                    groupAggregateProperty.setName(name);
                }
                aggregateProperties.add(groupAggregateProperty);
            } else {
                //直接的属性表达式
                groupAggregateProperty.setAlias(fixCleanAlias(prop));
                groupAggregateProperty.setLabel(prop);
                groupAggregateProperty.setName(prop);
                groupProperties.add(groupAggregateProperty);
            }
        }

        //分组属性 JPA Expression 集合
        List<Expression<?>> groupExpressions = groupProperties.stream().map(one -> {
            Expression<?> expression = buildExpression(root, criteriaBuilder, one.getName(), one.getAlias());
            one.setExpression(expression);
            return expression;
        }).collect(Collectors.toList());
        //分组聚合 JPA Expression 集合
        List<Expression<?>> aggregateExpressions = aggregateProperties.stream().map(one -> {
            Expression<?> expression = buildExpression(root, criteriaBuilder, one.getName(), one.getAlias());
            one.setExpression(expression);
            return expression;
        }).collect(Collectors.toList());
        //全部查询 JPA Expression 集合
        List<Selection<?>> selectExpressions = Lists.newArrayList(groupExpressions);
        selectExpressions.addAll(aggregateExpressions);

        //构建JPA SELECT
        CriteriaQuery<Tuple> select = criteriaQuery.multiselect(selectExpressions);

        //基于前端动态条件对象动态having条件组装
        Predicate[] havingPredicates = groupFilter.getFilters().stream().map(one -> {
            //把查询条件属性名称与聚合属性匹配，获取对应的Expression然后构造对应的Predicate
            String name = one.getConvertedPropertyName();
            for (GroupAggregateProperty agg : aggregateProperties) {
                if (name.equals(agg.getAlias()) || name.equals(agg.getName()) || name.equals(agg.getLabel())) {
                    one.setHaving(true);
                    return buildPredicateByExpression(agg.getExpression(), one, root, criteriaBuilder);
                }
            }
            return null;
        }).toArray(Predicate[]::new);
        if (ArrayUtils.isNotEmpty(havingPredicates)) {
            Predicate having = criteriaBuilder.and(havingPredicates);
            select.having(having);
        }

        //基于前端动态条件对象动态where条件组装，注意放在having处理之后以剔除掉having查询条件
        Predicate where = buildPredicatesFromFilters(groupFilter, root, criteriaQuery, criteriaBuilder);
        if (where != null) {
            select.where(where);
        }

        //分页和排序处理
        if (pageable != null && pageable.getSort() != null) {
            Iterator<Order> orders = pageable.getSort().iterator();
            List<javax.persistence.criteria.Order> jpaOrders = Lists.newArrayList();
            while (orders.hasNext()) {
                Expression expression = null;

                Order order = orders.next();
                String name = order.getProperty();

                //与聚合属性比较，获取对应的Expression
                for (GroupAggregateProperty agg : aggregateProperties) {
                    if (name.equals(agg.getAlias()) || name.equals(agg.getName()) || name.equals(agg.getLabel())) {
                        expression = agg.getExpression();
                        break;
                    }
                }

                //非聚合属性，按照标准的对象属性构造Expression
                if (expression == null) {
                    expression = buildExpression(root, criteriaBuilder, name, null);
                }

                //有效的Expression，追加排序
                if (expression != null) {
                    if (order.isAscending()) {
                        jpaOrders.add(criteriaBuilder.asc(expression));
                    } else {
                        jpaOrders.add(criteriaBuilder.desc(expression));
                    }
                }
            }
            select.orderBy(jpaOrders);
        }

        //追加分组参数
        select.groupBy(groupExpressions);

        //创建查询对象
        TypedQuery<Tuple> query = entityManager.createQuery(select);
        //动态追加分页参数
        if (pageable != null) {
            query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
            query.setMaxResults(pageable.getPageSize());
        }
        //获取结果集合，并组装为前端便于JSON序列化的Map结构
        List<Tuple> tuples = query.getResultList();
        List<Map<String, Object>> mapDatas = Lists.newArrayList();
        for (Tuple tuple : tuples) {
            Map<String, Object> data = Maps.newHashMap();
            for (GroupAggregateProperty groupAggregateProperty : groupProperties) {
                data.put(groupAggregateProperty.getLabel(), tuple.get(groupAggregateProperty.getAlias()));
            }
            for (GroupAggregateProperty groupAggregateProperty : aggregateProperties) {
                data.put(groupAggregateProperty.getLabel(), tuple.get(groupAggregateProperty.getAlias()));
            }
            mapDatas.add(data);
        }

        if (pageable == null) {
            return ExtPageRequest.buildPageResultFromList(mapDatas);
        } else {
            //TODO：目前有个限制未实现总记录数处理，直接返回一个固定大数字
            return new PageImpl(mapDatas, pageable, Integer.MAX_VALUE);
        }
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     *
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql      Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql) {
        return findByPageNativeSQL(pageable, sql, null);
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     *
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql      Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @param orderby  order by部分
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql, String orderby) {
        Query query = null;
        if (StringUtils.isNotBlank(orderby)) {
            query = entityManager.createNativeQuery(sql + " " + orderby);
        } else {
            query = entityManager.createNativeQuery(sql);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryCount = entityManager.createNativeQuery("select count(*) from (" + sql + ") cnt");
        query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
        query.setMaxResults(pageable.getPageSize());
        Object count = queryCount.getSingleResult();
        return new PageImpl(query.getResultList(), pageable, Long.valueOf(count.toString()));
    }

    private <X> Predicate buildPredicate(String propertyName, PropertyFilter filter, Root<X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Object matchValue = filter.getMatchValue();
        String[] names = StringUtils.split(propertyName, ".");

        if (matchValue == null) {
            return null;
        }
        if (filter.isHaving()) {
            return null;
        }
        if (matchValue instanceof String) {
            if (StringUtils.isBlank(String.valueOf(matchValue))) {
                return null;
            }
        }

        if (filter.getMatchType().equals(MatchType.FETCH)) {
            if (names.length == 1) {
                JoinType joinType = JoinType.INNER;
                if (matchValue instanceof String) {
                    joinType = Enum.valueOf(JoinType.class, (String) matchValue);
                } else {
                    joinType = (JoinType) filter.getMatchValue();
                }

                // Hack for Bug: https://jira.springsource.org/browse/DATAJPA-105
                // 如果是在count计算总记录，则添加join；否则说明正常分页查询添加fetch
                if (Persistable.class.isAssignableFrom(query.getResultType())) {
                    root.fetch(names[0], joinType);
                } else {
                    root.join(names[0], joinType);
                }
            } else {
                JoinType[] joinTypes = new JoinType[names.length];
                if (matchValue instanceof String) {
                    String[] joinTypeSplits = StringUtils.split(String.valueOf(matchValue), ".");
                    Assert.isTrue(joinTypeSplits.length == names.length, filter.getMatchType() + " 操作属性个数和Join操作个数必须一致");
                    for (int i = 0; i < joinTypeSplits.length; i++) {
                        joinTypes[i] = Enum.valueOf(JoinType.class, joinTypeSplits[i].trim());
                    }
                } else {
                    joinTypes = (JoinType[]) filter.getMatchValue();
                    Assert.isTrue(joinTypes.length == names.length, filter.getMatchType() + " 操作属性个数和Join操作个数必须一致");
                }

                // Hack for Bug: https://jira.springsource.org/browse/DATAJPA-105
                // 如果是在count计算总记录，则添加join；否则说明正常分页查询添加fetch
                if (Persistable.class.isAssignableFrom(query.getResultType())) {
                    Fetch fetch = root.fetch(names[0], joinTypes[0]);
                    for (int i = 1; i < names.length; i++) {
                        fetch.fetch(names[i], joinTypes[i]);
                    }
                } else {
                    Join join = root.join(names[0], joinTypes[0]);
                    for (int i = 1; i < names.length; i++) {
                        join.join(names[i], joinTypes[i]);
                    }
                }
            }

            return null;
        }


        Expression expression = null;

        // 处理集合子查询
        Subquery<Long> subquery = null;
        Root subQueryFrom = null;
        if (filter.getSubQueryCollectionPropetyType() != null) {
            subquery = query.subquery(Long.class);
            subQueryFrom = subquery.from(filter.getSubQueryCollectionPropetyType());
            Path path = subQueryFrom.get(names[1]);
            if (names.length > 2) {
                for (int i = 2; i < names.length; i++) {
                    path = path.get(names[i]);
                }
            }
            expression = path;
        } else {
            expression = buildExpression(root, builder, propertyName, null);
        }

        Predicate predicate = buildPredicateByExpression(expression, filter, root, builder);

        //处理集合子查询
        if (filter.getSubQueryCollectionPropetyType() != null) {
            String owner = StringUtils.uncapitalize(entityClass.getSimpleName());
            subQueryFrom.join(owner);
            subquery.select(subQueryFrom.get(owner)).where(predicate);
            predicate = builder.in(root.get("id")).value(subquery);
        }

        return predicate;
    }

    /**
     * 基于表达式构建查询条件对象
     *
     * @param expression
     * @param filter
     * @param root
     * @param builder
     * @return
     */
    private Predicate buildPredicateByExpression(Expression expression, PropertyFilter filter, Root<?> root, CriteriaBuilder builder) {
        Object matchValue = filter.getMatchValue();
        Predicate predicate = null;

        //对于几个特殊字符串含义特殊处理
        if ("NULL".equalsIgnoreCase(String.valueOf(matchValue))) {
            return expression.isNull();
        } else if ("EMPTY".equalsIgnoreCase(String.valueOf(matchValue))) {
            return builder.or(builder.isNull(expression), builder.equal(expression, ""));
        } else if ("NONULL".equalsIgnoreCase(String.valueOf(matchValue))) {
            return expression.isNotNull();
        } else if ("NOEMPTY".equalsIgnoreCase(String.valueOf(matchValue))) {
            return builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
        }

        // logic operator
        switch (filter.getMatchType()) {
            case EQ:
                // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                if (matchValue instanceof LocalDateTime) {
                    LocalDateTime date = (LocalDateTime) matchValue;
                    // 带时分秒数据直接追加对应条件
                    if (date.getHour() != 0 || date.getMinute() != 0 || date.getSecond() != 0) {
                        predicate = builder.equal(expression, date);
                    } else {
                        predicate = builder.and(builder.greaterThanOrEqualTo(expression, date),
                                builder.lessThan(expression, date.plusDays(1)));
                    }
                } else {
                    predicate = builder.equal(expression, matchValue);
                }
                break;
            case NE:
                // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                if (matchValue instanceof LocalDateTime) {
                    LocalDateTime date = (LocalDateTime) matchValue;
                    // 带时分秒数据直接追加对应条件
                    if (date.getHour() != 0 || date.getMinute() != 0 || date.getSecond() != 0) {
                        predicate = builder.notEqual(expression, date);
                    } else {
                        predicate = builder.or(builder.lessThan(expression, date),
                                builder.greaterThanOrEqualTo(expression, date.plusDays(1)));
                    }
                } else {
                    predicate = builder.notEqual(expression, matchValue);
                }
                break;
            case BK:
                predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
                break;
            case NB:
                predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
                break;
            case NU:
                if (matchValue instanceof Boolean && (Boolean) matchValue == false) {
                    predicate = builder.isNotNull(expression);
                } else {
                    predicate = builder.isNull(expression);
                }
                break;
            case NN:
                if (matchValue instanceof Boolean && (Boolean) matchValue == false) {
                    predicate = builder.isNull(expression);
                } else {
                    predicate = builder.isNotNull(expression);
                }
                break;
            case CN:
                predicate = builder.like(expression, "%" + matchValue + "%");
                break;
            case NC:
                predicate = builder.notLike(expression, "%" + matchValue + "%");
                break;
            case BW:
                predicate = builder.like(expression, matchValue + "%");
                break;
            case BN:
                predicate = builder.notLike(expression, matchValue + "%");
                break;
            case EW:
                predicate = builder.like(expression, "%" + matchValue);
                break;
            case EN:
                predicate = builder.notLike(expression, "%" + matchValue);
                break;
            case BT:
                Assert.isTrue(matchValue.getClass().isArray(), "Match value must be array");
                Object[] matchValues = (Object[]) matchValue;
                Assert.isTrue(matchValues.length == 2, "Match value must have two value");
                if (matchValues[0] instanceof LocalDateTime) {
                    LocalDateTime dateTo = (LocalDateTime) matchValues[1];
                    // 带时分秒数据直接追加对应条件
                    if (dateTo.getHour() != 0 || dateTo.getMinute() != 0 || dateTo.getSecond() != 0) {
                        predicate = builder.between(expression, (Comparable) matchValues[0], (Comparable) matchValues[1]);
                    } else {
                        // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                        // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                        dateTo = dateTo.plusDays(1);
                        predicate = builder.and(builder.greaterThanOrEqualTo(expression, (LocalDateTime) matchValues[0]),
                                builder.lessThan(expression, dateTo));
                    }
                } else {
                    predicate = builder.between(expression, (Comparable) matchValues[0], (Comparable) matchValues[1]);
                }
                break;
            case GT:
                predicate = builder.greaterThan(expression, (Comparable) matchValue);
                break;
            case GE:
                predicate = builder.greaterThanOrEqualTo(expression, (Comparable) matchValue);
                break;
            case LT:
                predicate = builder.lessThan(expression, (Comparable) matchValue);
                break;
            case LE:
                if (matchValue instanceof LocalDateTime) {
                    LocalDateTime date = (LocalDateTime) matchValue;
                    // 带时分秒数据直接追加对应条件
                    if (date.getHour() != 0 || date.getMinute() != 0 || date.getSecond() != 0) {
                        predicate = builder.lessThanOrEqualTo(expression, date);
                    } else {
                        // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                        // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                        date = date.plusDays(1);
                        predicate = builder.lessThan(expression, date);
                    }
                } else {
                    predicate = builder.lessThanOrEqualTo(expression, (Comparable) matchValue);
                }
                break;
            case IN:
                if (matchValue.getClass().isArray()) {
                    predicate = expression.in((Object[]) matchValue);
                } else if (matchValue instanceof Collection) {
                    predicate = expression.in((Collection) matchValue);
                } else {
                    predicate = builder.equal(expression, matchValue);
                }
                break;
            case ACLPREFIXS:
                List<Predicate> aclPredicates = Lists.newArrayList();
                Collection<String> aclCodePrefixs = (Collection<String>) matchValue;
                for (String aclCodePrefix : aclCodePrefixs) {
                    if (StringUtils.isNotBlank(aclCodePrefix)) {
                        aclPredicates.add(builder.like(expression, aclCodePrefix + "%"));
                    }

                }
                if (aclPredicates.size() == 0) {
                    return null;
                }
                predicate = builder.or(aclPredicates.toArray(new Predicate[aclPredicates.size()]));
                break;
            case PLT:
                Expression expressionPLT = buildExpression(root, builder, (String) matchValue, null);
                predicate = builder.lessThan(expression, expressionPLT);
                break;
            case PLE:
                Expression expressionPLE = buildExpression(root, builder, (String) matchValue, null);
                predicate = builder.lessThanOrEqualTo(expression, expressionPLE);
                break;
            default:
                break;
        }


        Assert.notNull(predicate, "Undefined match type: " + filter.getMatchType());
        return predicate;
    }

    /**
     * 根据条件集合对象组装JPA规范条件查询集合对象，基类默认实现进行条件封装组合
     * 子类可以调用此方法在返回的List<Predicate>额外追加其他PropertyFilter不易表单的条件如exist条件处理等
     *
     * @param filters
     * @param root
     * @param query
     * @param builder
     * @return
     */
    private <X> List<Predicate> buildPredicatesFromFilters(final Collection<PropertyFilter> filters, Root<X> root, CriteriaQuery<?> query,
                                                           CriteriaBuilder builder) {
        List<Predicate> predicates = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (PropertyFilter filter : filters) {
                if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                    Predicate predicate = buildPredicate(filter.getConvertedPropertyName(), filter, root, query, builder);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                } else {// 包含多个属性需要比较的情况,进行or处理.
                    List<Predicate> orpredicates = Lists.newArrayList();
                    for (String param : filter.getConvertedPropertyNames()) {
                        Predicate predicate = buildPredicate(param, filter, root, query, builder);
                        if (predicate != null) {
                            orpredicates.add(predicate);
                        }
                    }
                    if (orpredicates.size() > 0) {
                        predicates.add(builder.or(orpredicates.toArray(new Predicate[orpredicates.size()])));
                    }
                }
            }
        }
        return predicates;
    }

    private <X extends Persistable> Specification<X> buildSpecification(final GroupPropertyFilter groupPropertyFilter) {
        if (BaseNativeNestedSetEntity.class.isAssignableFrom(entityClass)) {
            //排除Nested Set Model模型默认根节点
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NE, "depth", 0));
        }

        return (Specification<X>) (root, query, builder) -> {
            if (groupPropertyFilter != null) {
                return buildPredicatesFromFilters(groupPropertyFilter, root, query, builder);
            } else {
                return null;
            }
        };
    }

    protected Predicate buildPredicatesFromFilters(GroupPropertyFilter groupPropertyFilter, Root root, CriteriaQuery<?> query,
                                                   CriteriaBuilder builder) {
        if (groupPropertyFilter == null) {
            return null;
        }

        //根据 GroupPropertyFilter 嵌套数据结构，递归组装查询条件
        List<Predicate> predicates = buildPredicatesFromFilters(groupPropertyFilter.getFilters(), root, query, builder);
        if (CollectionUtils.isNotEmpty(groupPropertyFilter.getGroups())) {
            for (GroupPropertyFilter group : groupPropertyFilter.getGroups()) {
                if (CollectionUtils.isEmpty(group.getFilters()) && CollectionUtils.isEmpty(group.getForceAndFilters())) {
                    continue;
                }
                Predicate subPredicate = buildPredicatesFromFilters(group, root, query, builder);
                if (subPredicate != null) {
                    predicates.add(subPredicate);
                }
            }
        }

        //对各组查询条件集合进行AND或OR合并
        Predicate predicate = null;
        if (CollectionUtils.isNotEmpty(predicates)) {
            if (predicates.size() == 1) {
                predicate = predicates.get(0);
            } else {
                if (groupPropertyFilter.getGroupType().equals(GroupPropertyFilter.GroupOperationEnum.OR)) {
                    predicate = builder.or(predicates.toArray(new Predicate[predicates.size()]));
                } else {
                    predicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            }
        }

        //额外强制追加的AND组合过滤条件处理
        List<Predicate> appendAndPredicates = buildPredicatesFromFilters(groupPropertyFilter.getForceAndFilters(), root, query, builder);
        if (CollectionUtils.isNotEmpty(appendAndPredicates)) {
            if (predicate != null) {
                appendAndPredicates.add(predicate);
            }
            predicate = builder.and(appendAndPredicates.toArray(new Predicate[appendAndPredicates.size()]));
        }

        return predicate;
    }

    private Expression parseExpr(Root<?> root, CriteriaBuilder criteriaBuilder, String expr, Map<String, Expression<?>> parsedExprMap) {
        if (parsedExprMap == null) {
            parsedExprMap = Maps.newHashMap();
        }
        Expression<?> expression = null;
        if (expr.indexOf("(") > -1) {
            int left = 0;
            char[] chars = expr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    left = i;
                }
            }
            String leftStr = expr.substring(0, left);
            String op = null;
            char[] leftStrs = leftStr.toCharArray();
            for (int i = leftStrs.length - 1; i > 0; i--) {
                if (leftStrs[i] == '(' || leftStrs[i] == ')' || leftStrs[i] == ',') {
                    op = leftStr.substring(i + 1);
                    break;
                }
            }
            if (op == null) {
                op = leftStr;
            }
            String rightStr = expr.substring(left + 1);
            String arg = StringUtils.substringBefore(rightStr, ")");
            String[] args = arg.split(",");
            //logger.debug("op={},arg={}", op, arg);
            if ("case".equalsIgnoreCase(op)) {
                Case selectCase = criteriaBuilder.selectCase();

                Expression caseWhen = parsedExprMap.get(args[0]);

                String whenResultExpr = args[1];
                Object whenResult = parsedExprMap.get(whenResultExpr);
                if (whenResult == null) {
                    Case<Long> whenCase = selectCase.when(caseWhen, new BigDecimal(whenResultExpr));
                    selectCase = whenCase;
                } else {
                    Case<Expression<?>> whenCase = selectCase.when(caseWhen, whenResult);
                    selectCase = whenCase;
                }
                String otherwiseResultExpr = args[2];
                Object otherwiseResult = parsedExprMap.get(otherwiseResultExpr);
                if (otherwiseResult == null) {
                    expression = selectCase.otherwise(new BigDecimal(otherwiseResultExpr));
                } else {
                    expression = selectCase.otherwise((Expression<?>) otherwiseResult);
                }
            } else {
                Object[] subExpressions = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    subExpressions[i] = parsedExprMap.get(args[i]);
                    if (subExpressions[i] == null) {
                        String name = args[i];
                        try {
                            Path<?> item = null;
                            if (name.indexOf(".") > -1) {
                                String[] props = StringUtils.split(name, ".");
                                item = root.get(props[0]);
                                for (int j = 1; j < props.length; j++) {
                                    item = item.get(props[j]);
                                }
                            } else {
                                item = root.get(name);
                            }
                            subExpressions[i] = item;
                        } catch (Exception e) {
                            subExpressions[i] = new BigDecimal(name);
                        }
                    }
                }
                try {
                    //criteriaBuilder.quot();
                    expression = (Expression) MethodUtils.invokeMethod(criteriaBuilder, op, subExpressions);
                } catch (Exception e) {
                    logger.error("Error for aggregate  setting ", e);
                }
            }

            String exprPart = op + "(" + arg + ")";
            String exprPartConvert = exprPart.replace(op + "(", op + "_").replace(arg + ")", arg + "_").replace(",", "_");
            expr = expr.replace(exprPart, exprPartConvert);
            parsedExprMap.put(exprPartConvert, expression);

            //嵌套逻辑递归调用
            if (expr.indexOf("(") > -1) {
                expression = parseExpr(root, criteriaBuilder, expr, parsedExprMap);
            }
        } else {
            String name = expr;
            Path<?> item;
            if (name.indexOf(".") > -1) {
                String[] props = StringUtils.split(name, ".");
                item = root.get(props[0]);
                for (int j = 1; j < props.length; j++) {
                    item = item.get(props[j]);
                }
            } else {
                item = root.get(name);
            }
            expression = item;
        }
        return expression;
    }

    private String fixCleanAlias(String name) {
        return StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(name, "("), ")"), "."), ","), "-");
    }

    private Expression<?> buildExpression(Root<?> root, CriteriaBuilder criteriaBuilder, String name, String alias) {
        Expression<?> expr = parseExpr(root, criteriaBuilder, name, null);
        if (alias != null) {
            expr.alias(alias);
        }
        return expr;
    }

    public void detach(Object entity) {
        entityManager.detach(entity);
    }
}
