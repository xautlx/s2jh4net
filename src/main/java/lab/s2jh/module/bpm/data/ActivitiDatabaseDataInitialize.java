package lab.s2jh.module.bpm.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Activiti数据库基础数据初始化处理器
 */
public class ActivitiDatabaseDataInitialize {

    private final Logger logger = LoggerFactory.getLogger(ActivitiDatabaseDataInitialize.class);

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
                connection.prepareStatement("select count(1) from ACT_ID_USER").execute();
                logger.info("Table ACT_ID_USER exist, skipped.");
            } catch (Exception e) {
                logger.info("VIEW ACT_ID_USER NOT exist, Initializing Activiti Identity DDL...");
                //根据不同数据库类型执行不同初始化SQL脚本
                ClassPathResource resource = new ClassPathResource("lab/s2jh/module/bpm/data/ddl_activiti.sql");
                ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                resourceDatabasePopulator.populate(connection);
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
