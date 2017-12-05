package com.entdiy.schedule.web;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.BaseController;
import com.entdiy.schedule.entity.JobRunHist;
import com.entdiy.schedule.service.JobRunHistService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/schedule/job-run-hist")
public class JobRunHistController extends BaseController<JobRunHist, Long> {

    @Autowired
    private JobRunHistService jobRunHistService;

    @Override
    protected BaseService<JobRunHist, Long> getEntityService() {
        return jobRunHistService;
    }

    @MenuData("配置管理:计划任务管理:任务运行记录")
    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/schedule/jobRunHist-index";
    }

    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<JobRunHist> findByPage(HttpServletRequest request) {
        return super.findByPage(JobRunHist.class, request);
    }

    @RequiresPermissions("配置管理:计划任务管理:任务运行记录")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String htmlPreview() {
        return "admin/schedule/jobRunHist-view";
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }
}
