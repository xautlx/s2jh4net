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
package com.entdiy.schedule;

import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.schedule.entity.JobRunHist;
import com.entdiy.schedule.service.JobRunHistFacadeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.plugins.history.LoggingJobHistoryPlugin;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 扩展实现LoggingJobHistoryPlugin约定接口
 * 转换Quartz提供的相关接口数据为ScheduleJobRunHist对象并调用对应的Service接口把数据写入数据库表中
 */
public class ExtLoggingJobHistoryPlugin extends LoggingJobHistoryPlugin {

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        super.jobWasExecuted(context, jobException);

        Trigger trigger = context.getTrigger();
        if (!ExtSchedulerFactoryBean.isTriggerLogRunHist(trigger)) {
            return;
        }

        JobRunHistFacadeService jobRunHistFacadeService = SpringContextHolder.getBean(JobRunHistFacadeService.class);
        JobRunHist jobRunHist = new JobRunHist();
        try {
            jobRunHist.setNodeId(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            jobRunHist.setNodeId("U/A");
        }
        jobRunHist.setTriggerGroup(trigger.getKey().getGroup());
        jobRunHist.setTriggerName(trigger.getKey().getName());
        jobRunHist.setJobClass(context.getJobDetail().getJobClass().getName());
        jobRunHist.setJobName(context.getJobDetail().getKey().getName());
        jobRunHist.setJobGroup(context.getJobDetail().getKey().getGroup());
        jobRunHist.setFireTime(new java.util.Date());
        jobRunHist.setPreviousFireTime(trigger.getPreviousFireTime());
        jobRunHist.setNextFireTime(trigger.getNextFireTime());
        jobRunHist.setRefireCount(new Integer(context.getRefireCount()));
        if (jobException != null) {
            jobRunHist.setExceptionFlag(Boolean.TRUE);
            jobRunHist.setResult(jobException.getMessage());
            StringWriter strWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(new BufferedWriter(strWriter));
            jobException.printStackTrace(writer);
            writer.flush();
            strWriter.flush();
            String exceptionStack = strWriter.getBuffer().toString();
            jobRunHist.setExceptionStack(exceptionStack);
        } else {
            jobRunHist.setExceptionFlag(Boolean.FALSE);
            if (context.getResult() == null) {
                jobRunHist.setResult("SUCCESS");
            } else {
                String result = String.valueOf(context.getResult());
                jobRunHist.setResult(result);
            }
        }
        jobRunHistFacadeService.saveWithAsyncAndNewTransition(jobRunHist);
    }

}