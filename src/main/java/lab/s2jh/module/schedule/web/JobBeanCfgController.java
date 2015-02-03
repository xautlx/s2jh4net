package lab.s2jh.module.schedule.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.schedule.entity.JobBeanCfg;
import lab.s2jh.module.schedule.service.JobBeanCfgService;

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
@RequestMapping(value = "/admin/schedule/job-bean-cfg")
public class JobBeanCfgController extends BaseController<JobBeanCfg, Long> {

    @Autowired
    private JobBeanCfgService jobBeanCfgService;

    @Override
    protected BaseService<JobBeanCfg, Long> getEntityService() {
        return jobBeanCfgService;
    }

    @Override
    protected JobBeanCfg buildBindingEntity() {
        return new JobBeanCfg();
    }

    @MenuData("配置管理:计划任务管理:可配置任务管理")
    @RequiresPermissions("计划任务管理:可配置任务管理:100主界面")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/schedule/jobBeanCfg-index";
    }

    @RequiresPermissions("计划任务管理:可配置任务管理:110列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<JobBeanCfg> findByPage(HttpServletRequest request) {
        return super.findByPage(JobBeanCfg.class, request);
    }

    @RequiresPermissions("计划任务管理:可配置任务管理:200保存")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") JobBeanCfg entity, HttpServletRequest request) {
        return super.editSave(entity);
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
