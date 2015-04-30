package lab.s2jh.core.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

/**
 * 模拟实体对象实例构造帮助类
 */
public class MockEntityUtils {

    private final static Logger logger = LoggerFactory.getLogger(MockEntityUtils.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <X> X buildMockObject(Class<X> clazz) {
        X x = null;
        try {
            x = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                String mn = method.getName();
                if (mn.startsWith("set")) {
                    Class[] parameters = method.getParameterTypes();
                    if (parameters.length == 1) {
                        Method getMethod = MethodUtils.getAccessibleMethod(clazz, "get" + mn.substring(3), null);
                        if (getMethod != null) {
                            if (getMethod.getName().equals("getId")) {
                                continue;
                            }
                            Object value = null;
                            Class parameter = parameters[0];
                            if (parameter.isAssignableFrom(String.class)) {
                                Column column = getMethod.getAnnotation(Column.class);
                                int columnLength = 10;
                                if (column != null && column.length() < columnLength) {
                                    columnLength = column.length();
                                }
                                Size size = getMethod.getAnnotation(Size.class);
                                if (size != null && size.max() < columnLength) {
                                    columnLength = size.max();
                                }
                                value = RandomStringUtils.randomAlphabetic(columnLength);
                            } else if (parameter.isAssignableFrom(Date.class)) {
                                value = new Date();
                            } else if (parameter.isAssignableFrom(BigDecimal.class)) {
                                value = new BigDecimal(10 + new Random().nextDouble() * 1000);
                            } else if (parameter.isAssignableFrom(Integer.class)) {
                                value = new Random().nextInt();
                            } else if (parameter.isAssignableFrom(Boolean.class)) {
                                value = new Random().nextBoolean();
                            } else if (parameter.isEnum()) {
                                Method m = parameter.getDeclaredMethod("values", null);
                                Object[] result = (Object[]) m.invoke(parameter.getEnumConstants()[0], null);
                                value = result[new Random().nextInt(result.length)];
                            }
                            if (value != null) {
                                MethodUtils.invokeMethod(x, mn, value);
                                logger.trace("{}={}", method.getName(), value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return x;
    }

    /**
     * 判定实体对象对应表是否为空
     */
    public static boolean isEmptyTable(Class<?> entity, EntityManager entityManager) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        if (count == null || String.valueOf(count).equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据持久化
     * @param entity 待持久化对象实例
     * @param existCheckFields 可变参数，提供用于检查数据是否已存在的字段名称列表
     * @return
     */
    public static boolean persistSilently(EntityManager entityManager, Object entity, String... existCheckFields) {
        try {
            if (existCheckFields != null && existCheckFields.length > 0) {
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(entity.getClass());
                Root<?> root = criteriaQuery.from(entity.getClass());
                List<Predicate> predicatesList = new ArrayList<Predicate>();
                Map<String, Object> predicates = Maps.newHashMap();
                for (String field : existCheckFields) {
                    Object value = FieldUtils.readField(entity, field, true);
                    predicates.put(field, value);
                    if (value == null) {
                        predicatesList.add(criteriaBuilder.isNull(root.get(field)));
                    } else {
                        predicatesList.add(criteriaBuilder.equal(root.get(field), value));
                    }
                }
                criteriaQuery.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
                List<?> list = entityManager.createQuery(criteriaQuery).getResultList();
                if (list != null && list.size() > 0) {
                    logger.debug("Skipped exist data: {} -> {}", entity.getClass(), predicates);
                    return false;
                }
            }
            entityManager.persist(entity);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 初始化自增对象起始值
     */
    public static void autoIncrementInitValue(final Class<?> entity, final EntityManager entityManager) {
        if (!MockEntityUtils.isEmptyTable(entity, entityManager)) {
            logger.debug("Skipped autoIncrementInitValue as exist data: {}", entity.getClass());
            return;
        }
        Session session = entityManager.unwrap(Session.class);
        session.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                Table table = entity.getAnnotation(Table.class);
                MetaData metaData = entity.getAnnotation(MetaData.class);
                Assert.isTrue(metaData.autoIncrementInitValue() > 1, "Undefined MetaData autoIncrementInitValue for entity: " + entity.getClass());

                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String name = databaseMetaData.getDatabaseProductName().toLowerCase();
                //根据不同数据库类型执行不同初始化SQL脚本
                String sql = null;
                if (name.indexOf("mysql") > -1) {
                    sql = "ALTER TABLE " + table.name() + " AUTO_INCREMENT =" + metaData.autoIncrementInitValue();
                } else if (name.indexOf("h2") > -1) {

                }
                //TODO 其他数据库

                if (StringUtils.isNotBlank(sql)) {
                    logger.debug("Execute autoIncrementInitValue SQL: {}", sql);
                    entityManager.createNativeQuery(sql).executeUpdate();
                }
            }
        });

    }

    public static class TestVO {
        private String str;
        private Date dt;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public Date getDt() {
            return dt;
        }

        public void setDt(Date dt) {
            this.dt = dt;
        }
    }

    public static void main(String[] args) {
        TestVO testVO = MockEntityUtils.buildMockObject(TestVO.class);
        System.out.println("Mock Entity: " + ReflectionToStringBuilder.toString(testVO, ToStringStyle.MULTI_LINE_STYLE));
    }
}
