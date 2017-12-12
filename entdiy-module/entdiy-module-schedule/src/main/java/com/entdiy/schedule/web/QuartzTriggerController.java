/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.schedule.web;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.schedule.ExtSchedulerFactoryBean;
import com.entdiy.schedule.service.JobBeanCfgService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/schedule/quartz-trigger")
public class QuartzTriggerController {

    private static Logger logger = LoggerFactory.getLogger(QuartzTriggerController.class);

    @Autowired
    private JobBeanCfgService jobBeanCfgService;

    @MenuData("配置管理:计划任务管理:任务实时控制")
    @RequiresPermissions("配置管理:计划任务管理:任务实时控制")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/schedule/quartzTrigger-index";
    }

    @RequiresPermissions("配置管理:计划任务管理:任务实时控制")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(HttpServletRequest request) throws SchedulerException {
        List<Map<String, Object>> triggerDatas = Lists.newArrayList();
        Map<Trigger, SchedulerFactoryBean> allTriggers = jobBeanCfgService.findAllTriggers();
        for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
            Trigger trigger = me.getKey();
            ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Map<String, Object> triggerMap = Maps.newHashMap();
            triggerMap.put("id", trigger.getJobKey().getName());
            triggerMap.put("jobName", trigger.getJobKey().getName());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                triggerMap.put("cronExpression", cronTrigger.getCronExpression());
                triggerMap.put("previousFireTime", DateUtils.formatTime(cronTrigger.getPreviousFireTime()));
                triggerMap.put("nextFireTime", DateUtils.formatTime(cronTrigger.getNextFireTime()));
            }
            triggerMap.put("stateLabel", scheduler.getTriggerState(trigger.getKey()));
            triggerMap.put("runWithinCluster", schedulerFactoryBean.isRunWithinCluster());
            triggerDatas.add(triggerMap);
        }
        return new PageImpl(triggerDatas);
    }

    @RequiresPermissions("配置管理:计划任务管理:任务实时控制")
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult doStateTrigger(@RequestParam(value = "ids") String[] ids, @RequestParam(value = "state") String state)
            throws SchedulerException {
        Map<Trigger, SchedulerFactoryBean> allTriggers = jobBeanCfgService.findAllTriggers();
        for (String id : ids) {
            boolean exist = false;
            for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                Trigger trigger = me.getKey();
                if (trigger.getJobKey().getName().equals(id)) {
                    exist = true;
                    if ("pause".equals(state)) {
                        me.getValue().getScheduler().pauseTrigger(trigger.getKey());
                    } else if ("resume".equals(state)) {
                        me.getValue().getScheduler().resumeTrigger(trigger.getKey());
                    } else {
                        throw new UnsupportedOperationException("state parameter [" + state + "] not in [pause, resume]");
                    }
                    logger.debug("Switched trigger {} to state {}", id, state);
                    break;
                }
            }
            Validation.isTrue(exist, "Undefined trigger name: " + id);
        }
        return OperationResult.buildSuccessResult("批量状态更新操作完成");
    }

    @RequiresPermissions("配置管理:计划任务管理:任务实时控制")
    @RequestMapping(value = "/run", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult doRunTrigger(@RequestParam(value = "ids") String[] ids) throws SchedulerException {
        Map<Trigger, SchedulerFactoryBean> allTriggers = jobBeanCfgService.findAllTriggers();
        for (String id : ids) {
            for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                Trigger trigger = me.getKey();
                if (trigger.getJobKey().getName().equals(id)) {
                    ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    if (!scheduler.isStarted()) {
                        scheduler.start();
                    }
                    scheduler.triggerJob(trigger.getJobKey());
                    break;
                }
            }
        }
        return OperationResult.buildSuccessResult("立即执行计划任务作业操作完成");
    }
}
