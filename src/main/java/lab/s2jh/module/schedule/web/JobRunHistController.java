package lab.s2jh.module.schedule.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.module.schedule.entity.JobRunHist;
import lab.s2jh.module.schedule.service.JobRunHistService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/schedule/job-run-hist")
public class JobRunHistController extends BaseController<JobRunHist, Long> {

    @Autowired
    private JobRunHistService jobRunHistService;

    @Override
    protected BaseService<JobRunHist, Long> getEntityService() {
        return jobRunHistService;
    }

    @Override
    protected JobRunHist buildBindingEntity() {
        return new JobRunHist();
    }

    @MenuData("配置管理:计划任务管理:任务运行记录")
    @RequiresPermissions("计划任务管理:任务运行记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/schedule/jobRunHist-index";
    }

    @RequiresPermissions("计划任务管理:任务运行记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<JobRunHist> findByPage(HttpServletRequest request) {
        return super.findByPage(JobRunHist.class, request);
    }

    @RequiresPermissions("计划任务管理:任务运行记录")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String htmlPreview() {
        return "admin/schedule/jobRunHist-view";
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
