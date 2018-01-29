/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.schedule;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.entdiy.schedule.entity.JobBeanCfg;
import com.entdiy.schedule.service.JobBeanCfgService;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 扩展标准的SchedulerFactoryBean，实现基于数据库配置的任务管理器初始化
 */
public class ExtSchedulerFactoryBean extends SchedulerFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(ExtSchedulerFactoryBean.class);

    private ConfigurableApplicationContext applicationContext;

    private JobBeanCfgService jobBeanCfgService;

    private boolean runWithinCluster = false;

    public static Map<String, Boolean> TRIGGER_HIST_MAPPING = Maps.newHashMap();

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.runWithinCluster = true;
    }

    public boolean isRunWithinCluster() {
        return runWithinCluster;
    }

    public void setJobBeanCfgService(JobBeanCfgService jobBeanCfgService) {
        this.jobBeanCfgService = jobBeanCfgService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        super.setApplicationContext(applicationContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void registerJobsAndTriggers() throws SchedulerException {
        logger.debug("Invoking registerJobsAndTriggers...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        List<JobBeanCfg> jobBeanCfgs = jobBeanCfgService.findAll();
        List<Trigger> allTriggers = Lists.newArrayList();

        List<Trigger> triggers = null;
        try {
            //基于反射获取已经在XML中定义的triggers集合
            triggers = (List<Trigger>) FieldUtils.readField(this, "triggers", true);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        if (triggers == null) {
            triggers = Lists.newArrayList();
        } else {
            allTriggers.addAll(triggers);
        }

        for (JobBeanCfg jobBeanCfg : jobBeanCfgs) {
            // 只处理与当前Scheduler集群运行模式匹配的数据
            if (jobBeanCfg.getRunWithinCluster() == null || !jobBeanCfg.getRunWithinCluster().equals(runWithinCluster)) {
                continue;
            }
            // 以任务全类名作为Job和Trigger相关名称
            Class<?> jobClass = null;
            try {
                jobClass = Class.forName(jobBeanCfg.getJobClass());
            } catch (ClassNotFoundException e) {
                //容错处理避免由于配置错误导致无法启动应用
                logger.error(e.getMessage(), e);
            }
            if (jobClass == null) {
                continue;
            }
            String jobName = jobClass.getName();

            boolean jobExists = false;
            for (Trigger trigger : triggers) {
                if (trigger.getJobKey().getName().equals(jobName)) {
                    jobExists = true;
                    break;
                }
            }
            if (jobExists) {
                logger.warn("WARN: Skipped dynamic  job [{}] due to exists static configuration.", jobName);
                continue;
            }

            logger.debug("Build and schedule dynamical job： {}, CRON: {}", jobName, jobBeanCfg.getCronExpression());

            // Spring动态加载Job Bean
            BeanDefinitionBuilder bdbJobDetailBean = BeanDefinitionBuilder.rootBeanDefinition(JobDetailFactoryBean.class);
            bdbJobDetailBean.addPropertyValue("jobClass", jobBeanCfg.getJobClass());
            bdbJobDetailBean.addPropertyValue("durability", true);
            beanFactory.registerBeanDefinition(jobName, bdbJobDetailBean.getBeanDefinition());

            // Spring动态加载Trigger Bean
            String triggerName = jobName + ".Trigger";
            JobDetail jobDetailBean = (JobDetail) beanFactory.getBean(jobName);
            BeanDefinitionBuilder bdbCronTriggerBean = BeanDefinitionBuilder.rootBeanDefinition(CronTriggerFactoryBean.class);
            bdbCronTriggerBean.addPropertyValue("jobDetail", jobDetailBean);
            bdbCronTriggerBean.addPropertyValue("cronExpression", jobBeanCfg.getCronExpression());
            beanFactory.registerBeanDefinition(triggerName, bdbCronTriggerBean.getBeanDefinition());

            allTriggers.add((Trigger) beanFactory.getBean(triggerName));
        }

        this.setTriggers(allTriggers.toArray(new Trigger[] {}));
        super.registerJobsAndTriggers();

        for (Trigger trigger : allTriggers) {
            TRIGGER_HIST_MAPPING.put(trigger.getJobKey().getName(), true);
            for (JobBeanCfg jobBeanCfg : jobBeanCfgs) {
                if (jobBeanCfg.getJobClass().equals(trigger.getJobKey().getName())) {
                    // 把AutoStartup设定的计划任务初始设置为暂停状态
                    if (!jobBeanCfg.getAutoStartup()) {
                        logger.debug("Setup trigger {} state to PAUSE", trigger.getKey().getName());
                        this.getScheduler().pauseTrigger(trigger.getKey());
                    }
                    //设定是否开启日志记录
                    TRIGGER_HIST_MAPPING.put(trigger.getJobKey().getName(), jobBeanCfg.getLogRunHist());
                    break;
                }
            }
        }
    }

    public static boolean isTriggerLogRunHist(Trigger trigger) {
        Boolean hist = TRIGGER_HIST_MAPPING.get(trigger.getJobKey().getName());
        return hist == null ? true : hist;
    }
}
