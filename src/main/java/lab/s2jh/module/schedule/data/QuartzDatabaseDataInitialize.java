package lab.s2jh.module.schedule.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Quartz数据库基础数据初始化处理器
 */
public class QuartzDatabaseDataInitialize {

    private final Logger logger = LoggerFactory.getLogger(QuartzDatabaseDataInitialize.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        logger.info("Running " + this.getClass().getName());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            try {
                //尝试执行查询，如果异常则说明没有初始化
                connection.prepareStatement("select count(1) from QRTZ_TRIGGERS").execute();
                logger.info("Table QRTZ_TRIGGERS exist, skipped.");
            } catch (Exception e) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String name = databaseMetaData.getDatabaseProductName().toLowerCase();
                logger.info("Table QRTZ_TRIGGERS NOT exist, Initializing DDL for {}...", name);
                //根据不同数据库类型执行不同初始化SQL脚本
                ResourceDatabasePopulator resourceDatabasePopulator = null;
                String sqlFile = null;
                if (name.indexOf("mysql") > -1) {
                    sqlFile = "lab/s2jh/module/schedule/data/quartz/tables_mysql.sql";
                    ClassPathResource resource = new ClassPathResource(sqlFile);
                    resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                } else if (name.indexOf("h2") > -1) {
                    sqlFile = "lab/s2jh/module/schedule/data/quartz/tables_h2.sql";
                    ClassPathResource resource = new ClassPathResource(sqlFile);
                    resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                } else if (name.indexOf("microsoft") > -1 || name.indexOf("sql server") > -1) {
                    //In your Quartz properties file, you'll need to set 
                    //org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.MSSQLDelegate
                    sqlFile = "lab/s2jh/module/schedule/data/quartz/tables_sqlServer.sql";
                    ClassPathResource resource = new ClassPathResource(sqlFile);
                    resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                    resourceDatabasePopulator.setSeparator("GO");
                } else if (name.indexOf("oracle") > -1) {
                    //In your Quartz properties file, you'll need to set 
                    //org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.oracle.OracleDelegate
                    sqlFile = "lab/s2jh/module/schedule/data/quartz/tables_oracle.sql";
                    ClassPathResource resource = new ClassPathResource(sqlFile);
                    resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                }

                if (resourceDatabasePopulator != null) {
                    logger.info("Executing SQL Scripts: {}", sqlFile);
                    resourceDatabasePopulator.populate(connection);
                } else {
                    throw new UnsupportedOperationException("Undefined DatabaseProductName: " + name);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
