package lab.s2jh.module.schedule.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.schedule.ExtSchedulerFactoryBean;
import lab.s2jh.module.schedule.service.JobBeanCfgService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/admin/schedule/quartz-trigger")
public class QuartzTriggerController {

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
            triggerMap.put("runWithinCluster", schedulerFactoryBean.getRunWithinCluster());
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
            for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                Trigger trigger = me.getKey();
                if (trigger.getJobKey().getName().equals(id)) {
                    if (state.equals("pause")) {
                        me.getValue().getScheduler().pauseTrigger(trigger.getKey());
                    } else if (state.equals("resume")) {
                        me.getValue().getScheduler().resumeTrigger(trigger.getKey());
                    } else {
                        throw new UnsupportedOperationException("state parameter [" + state + "] not in [pause, resume]");
                    }
                    break;
                }
            }
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
