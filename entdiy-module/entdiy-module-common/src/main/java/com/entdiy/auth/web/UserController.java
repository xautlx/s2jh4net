package com.entdiy.auth.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.auth.entity.Privilege;
import com.entdiy.auth.entity.RoleR2Privilege;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.UserR2Role;
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.AuthUserDetails;
import com.entdiy.sys.service.MenuService;
import com.entdiy.sys.vo.NavMenuVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping(value = "/admin/auth/user")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private MenuService menuService;

    @Override
    protected BaseService<User, Long> getEntityService() {
        return userService;
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }

    @MenuData("配置管理:权限管理:用户账号")
    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("authTypeMap", EnumUtils.getEnumDataMap(AuthTypeEnum.class));
        return "admin/auth/user-index";
    }

    @RequiresPermissions(value={ "配置管理:权限管理:用户账号", "账户管理:新增推广账户" },logical=Logical.OR)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<User> findByPage(HttpServletRequest request) {
        return super.findByPage(User.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/auth/user-inputTabs";
    }

    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model, @ModelAttribute("entity") User entity) {
        model.addAttribute("authTypeMap", EnumUtils.getEnumDataMap(AuthTypeEnum.class));
        model.addAttribute("roles", roleService.findAllCached());
        if (entity.isNew()) {
            entity.setMgmtGranted(true);
        }
        return "admin/auth/user-inputBasic";
    }

    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") User entity, Model model,
            @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        Validation.notDemoMode();
        if (entity.isNew()) {
            Validation.isTrue(StringUtils.isNotBlank(rawPassword), "创建用户必须设置初始密码");
        }
        userService.saveCascadeR2Roles(entity, rawPassword);
        return OperationResult.buildSuccessResult("数据保存处理完成", entity);
    }

    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        Validation.notDemoMode();
        return super.delete(ids);
    }

    @MetaData(value = "汇总用户关联权限集合")
    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/privileges", method = RequestMethod.GET)
    public String privileges(Model model, @ModelAttribute("entity") User entity) {
        Set<Long> r2PrivilegeIds = Sets.newHashSet();
        List<Privilege> privileges = privilegeService.findAllCached();
        List<UserR2Role> userR2Roles = entity.getUserR2Roles();
        boolean isAdmin = false;
        for (UserR2Role r2 : userR2Roles) {
            if (r2.getRole().getCode().equals(AuthUserDetails.ROLE_SUPER_USER)) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            for (Privilege privilege : privileges) {
                r2PrivilegeIds.add(privilege.getId());
            }
        } else {
            for (UserR2Role userR2Role : userR2Roles) {
                List<RoleR2Privilege> roleR2Privileges = userR2Role.getRole().getRoleR2Privileges();
                for (RoleR2Privilege roleR2Privilege : roleR2Privileges) {
                    r2PrivilegeIds.add(roleR2Privilege.getPrivilege().getId());
                }
            }
        }
        model.addAttribute("r2PrivilegeIds", StringUtils.join(r2PrivilegeIds, ","));
        return "admin/auth/user-privileges";
    }

    @MetaData(value = "汇总用户关联菜单集合")
    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public String menusShow(Model model) {
        return "admin/auth/user-menus";
    }

    @RequiresPermissions("配置管理:权限管理:用户账号")
    @RequestMapping(value = "/menus/data", method = RequestMethod.GET)
    @ResponseBody
    public Object menusData(Model model, @ModelAttribute("entity") User entity) {
        List<Map<String, Object>> items = Lists.newArrayList();
        List<NavMenuVO> navMenuVOs = menuService.processUserMenu(entity);
        for (NavMenuVO navMenuVO : navMenuVOs) {
            //组装zTree结构数据
            Map<String, Object> item = Maps.newHashMap();
            item.put("id", navMenuVO.getId());
            item.put("pId", navMenuVO.getParentId());
            item.put("name", navMenuVO.getName());
            item.put("open", true);
            item.put("isParent", StringUtils.isBlank(navMenuVO.getUrl()));
            items.add(item);
        }
        return items;
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> tagsData(Model model, @RequestParam("q") String q) {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildDefaultOrGroupFilter();
        groupFilter.append(new PropertyFilter(MatchType.CN, "authUid", q));
        groupFilter.append(new PropertyFilter(MatchType.CN, "nickName", q));
        groupFilter.append(new PropertyFilter(MatchType.CN, "email", q));
        groupFilter.append(new PropertyFilter(MatchType.CN, "mobile", q));
        List<User> users = userService.findByFilters(groupFilter);
        List<Map<String, Object>> items = Lists.newArrayList();
        for (User user : users) {
            Map<String, Object> item = Maps.newHashMap();
            item.put("id", user.getAlias());
            item.put("text", user.getAlias());
            items.add(item);
        }
        return items;
    }
}
