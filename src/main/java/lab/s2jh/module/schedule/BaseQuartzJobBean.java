package lab.s2jh.module.schedule;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.module.schedule.entity.JobBeanCfg;
import lab.s2jh.module.schedule.service.JobBeanCfgService;
import lab.s2jh.support.service.FreemarkerService;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 自定义Quartz Job业务对象的基类定义
 * 业务Job继承此抽象基类，获得Spring ApplicationContext的能力从而可以获取Spring声明的Bean对象
 * 同时实现QuartzJobBean约定接口，编写定时处理逻辑
 */
public abstract class BaseQuartzJobBean extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(BaseQuartzJobBean.class);

    protected ApplicationContext applicationContext;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        logger.debug("Set applicationContext for QuartzJobBean");
        this.applicationContext = applicationContext;
    }

    protected <X> X getSpringBean(Class<X> clazz) {
        return this.applicationContext.getBean(clazz);
    }

    protected JdbcTemplate getJdbcTemplate() {
        return getSpringBean(JdbcTemplate.class);
    }

    /**
     * 基于Freemarker组装任务结果数据文本
     * @param context
     * @param dataMap
     * @return
     */
    protected String buildJobResultByTemplate(JobExecutionContext context, Map<String, Object> dataMap) {
        JobBeanCfgService jobBeanCfgService = getSpringBean(JobBeanCfgService.class);
        FreemarkerService freemarkerService = getSpringBean(FreemarkerService.class);

        JobBeanCfg jobBeanCfg = jobBeanCfgService.findByJobClass(context.getJobDetail().getJobClass().getName());
        if (jobBeanCfg != null) {
            String resultTemplate = jobBeanCfg.getResultTemplate();
            if (StringUtils.isNotBlank(resultTemplate)) {
                String result = freemarkerService.processTemplate(jobBeanCfg.getJobClass(), jobBeanCfg.getVersion(), resultTemplate, dataMap);
                return result;
            }
        }
        return "UNDEFINED";
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("Invoking executeInternalBiz for {}", this.getClass());

            //绑定JPA Session到当前线程
            EntityManagerFactory entityManagerFactory = getSpringBean(EntityManagerFactory.class);
            if (!TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(entityManager));
            }

            String result = executeInternalBiz(context);

            //解绑JPA Session从当前线程
            EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
            EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());

            if (context != null && StringUtils.isNotBlank(result)) {
                context.setResult(result);
            }
            logger.debug("Job execution result: {}", result);
        } catch (Exception e) {
            logger.error("Quartz job execution error", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * 定时任务内部逻辑实现，注意整个方法不在事务控制边界，因此如果涉及到多数据更新逻辑注意需要把所有业务逻辑封装到相关Service接口中，然后在此方法中一次性调用确保事务控制
     * @param context
     * @return 组装好的任务记录结果信息，可以调用 buildJobResultByTemplate 方法基于Freemarker模板组装复杂的响应文本信息
     */
    protected abstract String executeInternalBiz(JobExecutionContext context);
}
