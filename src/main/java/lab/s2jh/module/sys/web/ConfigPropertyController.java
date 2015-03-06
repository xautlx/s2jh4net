package lab.s2jh.module.sys.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.listener.ApplicationContextPostListener;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.service.ConfigPropertyService;

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
@RequestMapping(value = "/admin/sys/config-property")
public class ConfigPropertyController extends BaseController<ConfigProperty, Long> {

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    protected BaseService<ConfigProperty, Long> getEntityService() {
        return configPropertyService;
    }

    @Override
    protected ConfigProperty buildBindingEntity() {
        return new ConfigProperty();
    }

    @MenuData("配置管理:系统管理:参数配置")
    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/sys/configProperty-index";
    }

    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<ConfigProperty> findByPage(HttpServletRequest request) {
        return super.findByPage(ConfigProperty.class, request);
    }

    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow() {
        return "admin/sys/configProperty-inputBasic";
    }

    @RequiresPermissions("配置管理:系统管理:参数配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") ConfigProperty entity, HttpServletRequest request) {
        String key = entity.getPropKey();
        if (key.startsWith("cfg")) {
            ServletContext sc = request.getSession().getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, Object> globalCfg = (Map<String, Object>) sc.getAttribute(ApplicationContextPostListener.Application_Configuation_Value_Key);
            if (globalCfg.containsKey(entity.getPropKey())) {
                globalCfg.put(entity.getPropKey(), entity.getSimpleValue());
            }
        }
        return super.editSave(entity);
    }

    @RequestMapping(value = "/html-preview", method = RequestMethod.POST)
    public String htmlPreview() {
        return "admin/sys/configProperty-htmlPreview";
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
