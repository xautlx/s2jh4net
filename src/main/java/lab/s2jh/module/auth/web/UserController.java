package lab.s2jh.module.auth.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.EnumUtils;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.module.auth.service.RoleService;
import lab.s2jh.module.auth.service.UserService;

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
@RequestMapping(value = "/admin/auth/user")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    protected BaseService<User, Long> getEntityService() {
        return userService;
    }

    @Override
    protected User buildBindingEntity() {
        return new User();
    }

    @MenuData("配置管理:权限管理:用户账号")
    @RequiresPermissions("用户:100菜单")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("authType", writeValueAsJsonString(EnumUtils.getEnumDataMap(AuthTypeEnum.class)));
        return "admin/auth/user-index";
    }

    @RequiresPermissions("用户:110列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<User> findByPage(HttpServletRequest request) {
        return super.findByPage(User.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/auth/user-inputTabs";
    }

    @RequiresPermissions("用户:120基本信息编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        model.addAttribute("authTypeMap", EnumUtils.getEnumDataMap(AuthTypeEnum.class));
        model.addAttribute("roles", roleService.findAllCached());
        return "admin/auth/user-inputBasic";
    }

    @RequiresPermissions("用户:120基本信息编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") User entity, Model model,
            @RequestParam("rawPassword") String rawPassword) {
        userService.saveCascadeR2Roles(entity, rawPassword);
        return OperationResult.buildSuccessResult("数据保存处理完成", entity);
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
