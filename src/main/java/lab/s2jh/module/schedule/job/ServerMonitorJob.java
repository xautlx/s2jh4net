package lab.s2jh.module.schedule.job;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.module.schedule.BaseQuartzJobBean;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器监控统计任务（典型的Quartz单机（非集群）运行模式的任务）
 */
@MetaData("服务器监控统计")
public class ServerMonitorJob extends BaseQuartzJobBean {

    private final static Logger logger = LoggerFactory.getLogger(ServerMonitorJob.class);

    @Override
    protected String executeInternalBiz(JobExecutionContext context) {
        logger.debug("Just Mock: Monitor current server information, such as CPU, Memery...");
        return null;
    }

}
