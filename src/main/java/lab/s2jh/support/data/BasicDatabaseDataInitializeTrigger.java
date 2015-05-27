package lab.s2jh.support.data;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据库基础数据初始化处理器触发器
 */
@Component
public class BasicDatabaseDataInitializeTrigger {

    @Autowired
    private BasicDatabaseDataInitialize basicDatabaseDataInitialize;

    @PostConstruct
    public void initialize() throws Exception {
        basicDatabaseDataInitialize.initialize();
    }
}
