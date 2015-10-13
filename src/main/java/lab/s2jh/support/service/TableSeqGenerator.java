package lab.s2jh.support.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.exception.ServiceException;

import org.apache.shiro.util.ClassUtils;
import org.hibernate.LockMode;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.PooledLoOptimizer;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * 参考Hibernate的TableGenerator原理实现一个定制的业务流水号生成器
 * 
 * TableSeqGenerator seqGenerator = new TableSeqGenerator("ORDER_ID", 1000, 100);
 * Long nextVal = seqGenerator.generate(dataSource);
 */
public class TableSeqGenerator {

    private final int maxLo;

    private final String key;

    private Optimizer optimizer;

    private TableStructure tableStructure;

    /**
     * 
     * @param key 流水号类型唯一标识,如ORDER_ID
     * @param dialect 数据库方言
     * @param initialValue
     * @param incrementSize
     */
    public TableSeqGenerator(final String key, int initialValue, int incrementSize) {
        this.key = key;
        this.maxLo = incrementSize;
        if (maxLo >= 1) {
            optimizer = new PooledLoOptimizer(Long.class, maxLo);
        }
        this.tableStructure = new TableStructure(initialValue, incrementSize);

    }

    public synchronized Long generate(final DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            final Connection con = connection;
            // maxLo < 1 indicates a hilo generator with no hilo :?
            if (maxLo < 1) {
                //keep the behavior consistent even for boundary usages
                IntegralDataTypeHolder value = null;
                while (value == null || value.lt(0)) {
                    value = tableStructure.execute(con, key);
                }
                return value.makeValue().longValue();
            }

            Serializable nextVal = optimizer.generate(new AccessCallback() {
                public IntegralDataTypeHolder getNextValue() {
                    return tableStructure.execute(con, key);
                }

                @Override
                public String getTenantIdentifier() {
                    return key;
                }
            });

            return Long.valueOf(nextVal.toString());
        } catch (SQLException sqle) {
            throw new ServiceException(sqle.getMessage(), sqle);
        } finally {
            JdbcUtils.closeConnection(connection);
        }
    }

    /**
     * @see org.hibernate.id.enhanced.TableStructure
     *
     */
    private static class TableStructure {
        private final String tableName = "seq_generator_table";
        private final String valueColumnName = "next_val";
        private final int initialValue;
        private final int incrementSize;
        private final String selectQuery;
        private final String insertQuery;
        private final String updateQuery;

        @SuppressWarnings("deprecation")
        public TableStructure(int initialValue, int incrementSize) {
            this.initialValue = initialValue;
            this.incrementSize = incrementSize;

            //获取方言类型和对象
            HibernateJpaVendorAdapter jpaVendorAdapter = SpringContextHolder.getBean(HibernateJpaVendorAdapter.class);
            Dialect dialect = (Dialect) ClassUtils.newInstance((String) jpaVendorAdapter.getJpaPropertyMap().get(Environment.DIALECT));

            selectQuery = "select " + valueColumnName + " as id_val" + " from " + dialect.appendLockHint(LockMode.PESSIMISTIC_WRITE, tableName)
                    + " where code=? " + dialect.getForUpdateString();

            insertQuery = "insert into " + tableName + "(code," + valueColumnName + ",initial_value,increment_size) values (?,?,?,?)";

            updateQuery = "update " + tableName + " set " + valueColumnName + "= ?" + " where " + valueColumnName + "=? and code=?";
        }

        public IntegralDataTypeHolder execute(Connection connection, String key) {
            final IntegralDataTypeHolder value = IdentifierGeneratorHelper.getIntegralDataTypeHolder(Long.class);
            int rows;
            do {
                PreparedStatement selectStatement = null;
                try {
                    selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setString(1, key);
                    ResultSet selectRS = selectStatement.executeQuery();
                    if (!selectRS.next()) {
                        PreparedStatement insertPS = connection.prepareStatement(insertQuery);
                        insertPS.setString(1, key);
                        insertPS.setLong(2, initialValue);
                        insertPS.setLong(3, initialValue);
                        insertPS.setLong(4, incrementSize);
                        insertPS.executeUpdate();
                        JdbcUtils.closeStatement(insertPS);
                        value.initialize(initialValue);
                        return value;
                    } else {
                        value.initialize(selectRS, 1);
                    }
                    JdbcUtils.closeResultSet(selectRS);
                } catch (SQLException sqle) {
                    throw new ServiceException(sqle.getMessage(), sqle);
                } finally {
                    JdbcUtils.closeStatement(selectStatement);
                }

                PreparedStatement updatePS = null;
                try {
                    updatePS = connection.prepareStatement(updateQuery);
                    IntegralDataTypeHolder updateValue = value.copy().add(incrementSize);
                    updateValue.bind(updatePS, 1);
                    value.bind(updatePS, 2);
                    updatePS.setString(3, key);
                    rows = updatePS.executeUpdate();
                } catch (SQLException sqle) {
                    throw new ServiceException(sqle.getMessage(), sqle);
                } finally {
                    JdbcUtils.closeStatement(updatePS);
                }
            } while (rows == 0);

            return value;
        }
    }
}
