package lab.s2jh.module.sys.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.sys.entity.Menu;
import lab.s2jh.module.sys.service.MenuService;

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
@RequestMapping(value = "/admin/sys/menu")
public class MenuController extends BaseController<Menu, Long> {

    @Autowired
    private MenuService menuService;

    @Override
    protected BaseService<Menu, Long> getEntityService() {
        return menuService;
    }

    @Override
    protected Menu buildBindingEntity() {
        return new Menu();
    }

    @MenuData("配置管理:系统管理:菜单配置")
    @RequiresPermissions("配置管理:系统管理:菜单配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/sys/menu-index";
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmptySearch()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
    }

    @RequiresPermissions("配置管理:系统管理:菜单配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<Menu> findByPage(HttpServletRequest request) {
        return super.findByPage(Menu.class, request);
    }

    @RequiresPermissions("配置管理:系统管理:菜单配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") Menu entity, Model model) {
        return super.editSave(entity);
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
