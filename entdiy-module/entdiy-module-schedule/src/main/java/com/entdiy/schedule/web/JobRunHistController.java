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
package com.entdiy.schedule.web;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.schedule.entity.JobRunHist;
import com.entdiy.schedule.service.JobRunHistService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/schedule/job-run-hist")
public class JobRunHistController extends BaseController<JobRunHist, Long> {

    @Autowired
    private JobRunHistService jobRunHistService;

    @MenuData("配置管理:计划任务管理:任务运行记录")
    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/schedule/jobRunHist-index";
    }

    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonPage<JobRunHist> findByPage(@ModelPropertyFilter(JobRunHist.class) GroupPropertyFilter filter,
                                           @ModelPageableRequest Pageable pageable) {
        return jobRunHistService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String htmlPreview() {
        return "admin/schedule/jobRunHist-view";
    }
}
