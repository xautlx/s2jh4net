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
import lab.s2jh.core.audit.DefaultAuditable;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.shiro.util.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 模拟实体对象实例构造帮助类
 */
public class MockEntityUtils {

    private final static Logger logger = LoggerFactory.getLogger(MockEntityUtils.class);

    private final static RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    private final static Random random = new Random();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <X> X buildMockObject(Class<X> clazz) {
        X x = null;
        try {
            x = clazz.newInstance();
            for (Method method : clazz.getMethods()) {
                String mn = method.getName();
                if (mn.startsWith("set")) {
                    Class[] parameters = method.getParameterTypes();
                    if (parameters.length == 1) {
                        Method getMethod = MethodUtils.getAccessibleMethod(clazz, "get" + mn.substring(3), null);
                        if (getMethod != null) {
                            if (getMethod.getName().equals("getId")) {
                                continue;
                            }
                            //有默认值，则直接返回
                            if (MethodUtils.invokeMethod(x, getMethod.getName(), null, null) != null) {
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
                                value = DateUtils.currentDate();
                            } else if (parameter.isAssignableFrom(BigDecimal.class)) {
                                value = new BigDecimal(10 + new Double(new Random().nextDouble() * 1000).intValue());
                            } else if (parameter.isAssignableFrom(Integer.class)) {
                                value = 1 + new Double(new Random().nextDouble() * 100).intValue();
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
     * 随机取一个对象返回
     */
    public static <X> X randomCandidates(X... candidates) {
        List<X> list = Lists.newArrayList(candidates);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(randomDataGenerator.nextInt(0, list.size() - 1));
    }

    /**
     * 随机取一个对象返回
     */
    public static <X> X randomCandidates(Iterable<X> candidates) {
        List<X> list = Lists.newArrayList(candidates);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(randomDataGenerator.nextInt(0, list.size() - 1));
    }

    /**
     * 返回区间段随机整数
     * @param lower 最小值
     * @param upper 最大值
     * @return
     */
    public static int randomInt(int lower, int upper) {
        return randomDataGenerator.nextInt(lower, upper);
    }

    /**
     * 返回区间段随机整数
     * @param lower 最小值
     * @param upper 最大值
     * @return
     */
    public static long randomLong(int lower, int upper) {
        return randomDataGenerator.nextLong(lower, upper);
    }

    /**
     * 返回0-1区间段随机小数
     * @return
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * 返回区间段随机布尔值
     * @return
     */
    public static boolean randomBoolean() {
        return randomDataGenerator.nextInt(0, 100) > 50 ? true : false;
    }

    /**
     * 返回区间段随机日期
     * @param daysBeforeNow 距离当前日期之前天数
     * @param daysAfterNow 距离当前日期之后天数
     * @return
     */
    public static Date randomDate(int daysBeforeNow, int daysAfterNow) {
        DateTime dt = new DateTime();
        dt = dt.plusMinutes(randomInt(-30, 30));
        dt = dt.plusHours(randomInt(-12, 12));
        dt = dt.minusDays(daysBeforeNow);
        dt = dt.plusDays(randomInt(0, daysBeforeNow + daysAfterNow));
        return dt.toDate();
    }

    /**
     * 获取表数据总记录数
     */
    public static int countTable(Class<?> entity, EntityManager entityManager) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        return Integer.valueOf(String.valueOf(count));
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
     * 判定实体对象对应表是否为空
     */
    public static <X> List<X> findAll(Class<X> entity, EntityManager entityManager) {
        return entityManager.createQuery("from " + entity.getSimpleName()).getResultList();
    }

    /**
     * 数据持久化
     * @param entity 待持久化对象实例
     * @return
     */
    public static void persistNew(EntityManager entityManager, Object entity) {
        entityManager.persist(entity);
        //特殊处理SaveUpdateAuditListener的CreatedDate“篡改”为当前临时系统时间
        if (entity instanceof DefaultAuditable) {
            ((DefaultAuditable) entity).setCreatedDate(DateUtils.currentDate());
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
            entityManager.flush();
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
                } else if (name.indexOf("sql server") > -1) {
                    //DBCC   CHECKIDENT( 'tb ',   RESEED,   20000)  
                    sql = "DBCC CHECKIDENT('" + table.name() + "',RESEED," + metaData.autoIncrementInitValue() + ")";
                } else if (name.indexOf("h2") > -1) {
                    //DO Nothing;
                } else {
                    throw new UnsupportedOperationException(name);
                }

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
