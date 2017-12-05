package com.entdiy.core.dao.router;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 */
public class RandomRoutingDataSource extends AbstractDataSource implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RandomRoutingDataSource.class);

    private final static RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    private List<DataSource> dataSources;

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    protected DataSource determineTargetDataSource() {
        int index = 0;
        if (dataSources.size() > 1) {
            //如果对应多个数据源，则随机挑选一个
            index = randomDataGenerator.nextInt(0, dataSources.size() - 1);
        }
        DataSource dataSource = dataSources.get(index);
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        DataSource dataSource = determineTargetDataSource();
        Connection connection = dataSource.getConnection();
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    public void afterPropertiesSet() {
        if (this.dataSources == null) {
            throw new IllegalArgumentException("Property 'dataSources' is required");
        }
        logger.info("Using RandomRoutingDataSource size: {}", dataSources.size());
    }
}
