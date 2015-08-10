package lab.s2jh.core.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库数据初始化处理器触发器
 */
public class DatabaseDataInitializeTrigger {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeTrigger.class);

    private List<BaseDatabaseDataInitialize> initializeProcessors;

    public void setInitializeProcessors(List<BaseDatabaseDataInitialize> initializeProcessors) {
        this.initializeProcessors = initializeProcessors;
    }

    public void initialize() {
        for (BaseDatabaseDataInitialize initializeProcessor : initializeProcessors) {
            logger.debug("Invoking data initialize for {}", initializeProcessor);
            try {
                initializeProcessor.initialize();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

}
