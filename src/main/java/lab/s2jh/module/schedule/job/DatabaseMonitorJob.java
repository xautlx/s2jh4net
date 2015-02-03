package lab.s2jh.module.schedule.job;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.module.schedule.BaseQuartzJobBean;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库监控统计任务（典型的Quartz集群运行模式的任务）
 */
@MetaData("数据库监控统计")
public class DatabaseMonitorJob extends BaseQuartzJobBean {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseMonitorJob.class);

    @Override
    protected String executeInternalBiz(JobExecutionContext context) {
        logger.debug("Just Mock: Monitor database information running with Spring Quartz cluster mode...");
        return null;
    }

}
