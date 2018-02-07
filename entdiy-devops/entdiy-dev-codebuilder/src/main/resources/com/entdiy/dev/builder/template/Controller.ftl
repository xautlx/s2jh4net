package ${root_package}.web.admin;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.core.web.json.JsonViews;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;

import com.fasterxml.jackson.annotation.JsonView;

@MetaData("${model_title}管理")
@Controller
@RequestMapping(value = "/admin${model_path}/${entity_name_field_line}")
public class ${entity_name}Controller extends BaseController<${entity_name},${id_type}> {

    @Autowired
    private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @Override
    protected BaseService<${entity_name}, ${id_type}> getEntityService() {
        return ${entity_name_uncapitalize}Service;
    }
    
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }
    
    @MenuData("${model_title}")
    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity ${entity_name} entity, Model model) {
        return "admin${model_path}/${entity_name_uncapitalize}-index";
    }   
    
    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<${entity_name}> findByPage(HttpServletRequest request) {
        return super.findByPage(${entity_name}.class, request);
    }
    
    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(@ModelEntity User entity, HttpServletRequest request) {
        return "admin${model_path}/${entity_name_uncapitalize}-inputTabs";
    }

    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        return "admin${model_path}/${entity_name_uncapitalize}-inputBasic";
    }

    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity ${entity_name} entity, Model model) {
        return super.editSave(entity);
    }

    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids);
    }
}