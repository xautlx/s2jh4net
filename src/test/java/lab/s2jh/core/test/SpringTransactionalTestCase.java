package lab.s2jh.core.test;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lab.s2jh.core.context.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Spring的支持数据库访问, 事务控制和依赖注入的JUnit4 集成测试基类.
 * 相比Spring原基类名字更短并保存了dataSource变量.
 *   
 * 子类需要定义applicationContext文件的位置, 如:
 * @ContextConfiguration(locations = { "classpath*:/context/spring-bpm.xml" })
 * 
 */
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:/context/context-profiles.xml", "classpath:/context/spring*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public abstract class SpringTransactionalTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected DataSource dataSource;

    @PostConstruct
    public void init() {
        SpringContextHolder.setApplicationContext(applicationContext);
    }

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

}
