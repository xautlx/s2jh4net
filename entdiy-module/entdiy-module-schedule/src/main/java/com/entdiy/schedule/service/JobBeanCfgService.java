/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.schedule.service;

import java.util.List;
import java.util.Map;

import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.BaseService;
import com.entdiy.schedule.ExtSchedulerFactoryBean;
import com.entdiy.schedule.dao.JobBeanCfgDao;
import com.entdiy.schedule.entity.JobBeanCfg;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class JobBeanCfgService extends BaseService<JobBeanCfg, Long> {

    private static Logger logger = LoggerFactory.getLogger(JobBeanCfgService.class);

    @Autowired
    private JobBeanCfgDao jobBeanCfgDao;

    @Override
    protected BaseDao<JobBeanCfg, Long> getEntityDao() {
        return jobBeanCfgDao;
    }

    public List<JobBeanCfg> findAll() {
        return jobBeanCfgDao.findAll();
    }

    public JobBeanCfg findByJobClass(String jobClass) {
        return jobBeanCfgDao.findByJobClass(jobClass);
    }

    @SuppressWarnings({ "unused", "unchecked" })
    public Map<Trigger, SchedulerFactoryBean> findAllTriggers() {
        Map<Trigger, SchedulerFactoryBean> allTriggers = Maps.newLinkedHashMap();
        try {
            SchedulerFactoryBean quartzRAMScheduler = (SchedulerFactoryBean) SpringContextHolder.getApplicationContext().getBean(
                    "&quartzRAMScheduler");
            if (quartzRAMScheduler != null) {
                for (Trigger trigger : (List<Trigger>) FieldUtils.readField(quartzRAMScheduler, "triggers", true)) {
                    allTriggers.put(trigger, quartzRAMScheduler);
                }
            }

            SchedulerFactoryBean quartzClusterScheduler = (SchedulerFactoryBean) SpringContextHolder.getApplicationContext().getBean(
                    "&quartzClusterScheduler");
            if (quartzClusterScheduler != null) {
                Scheduler scheduler = quartzClusterScheduler.getScheduler();
                for (Trigger trigger : (List<Trigger>) FieldUtils.readField(quartzClusterScheduler, "triggers", true)) {
                    allTriggers.put(trigger, quartzClusterScheduler);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        return allTriggers;
    }

    @Override
    public JobBeanCfg save(JobBeanCfg entity) {
        try {
            if (!entity.isNew()) {// 新配置任务不做Schedule处理，需要重新启动应用服务器才能生效
                Map<Trigger, SchedulerFactoryBean> allTriggers = findAllTriggers();
                for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                    CronTrigger cronTrigger = (CronTrigger) me.getKey();
                    ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    if (cronTrigger.getJobKey().getName().equals(entity.getJobClass())
                            && !entity.getCronExpression().equals(cronTrigger.getCronExpression())) {
                        CronTrigger newTrigger = TriggerBuilder.newTrigger().withIdentity(cronTrigger.getKey())
                                .withSchedule(CronScheduleBuilder.cronSchedule(entity.getCronExpression())).build();
                        String oldCronExpression = cronTrigger.getCronExpression();
                        logger.info("RescheduleJob : {}, CRON from {} to {}", cronTrigger.getKey(), oldCronExpression,
                                cronTrigger.getCronExpression());
                        scheduler.rescheduleJob(newTrigger.getKey(), newTrigger);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        return super.save(entity);
    }

    @Override
    public void delete(JobBeanCfg entity) {
        try {
            Map<Trigger, SchedulerFactoryBean> allTriggers = findAllTriggers();
            for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                CronTrigger cronTrigger = (CronTrigger) me.getKey();
                ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                if (cronTrigger.getJobKey().getName().equals(entity.getJobClass())) {
                    logger.info("UnscheduleJob from quartzClusterScheduler: {}", cronTrigger.getJobKey());
                    scheduler.unscheduleJob(cronTrigger.getKey());
                    break;
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        super.delete(entity);
    }

}
