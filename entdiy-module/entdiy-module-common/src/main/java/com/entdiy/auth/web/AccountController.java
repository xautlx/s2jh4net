package com.entdiy.auth.web;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.EnumUtils;
import com.entdiy.core.util.JsonUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.view.OperationResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@MetaData("登录账户")
@Controller
@RequestMapping(value = "/admin/auth/account")
public class AccountController extends BaseController<Account, Long> {

    @Autowired
    private AccountService accountService;

    @Override
    protected BaseService<Account, Long> getEntityService() {
        return accountService;
    }

    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }

    @MenuData("配置管理:权限管理:登录账户管理")
    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("authTypeJson", JsonUtils.writeValueAsString(EnumUtils.getEnumDataMap(Account.AuthTypeEnum.class)));
        return "admin/auth/account-index";
    }

    @RequiresPermissions(value = {"配置管理:权限管理:登录账户管理"}, logical = Logical.OR)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<Account> findByPage(HttpServletRequest request) {
        return super.findByPage(entityClass, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "admin/auth/account-inputTabs";
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model, @ModelAttribute("entity") Account entity) {
        return "admin/auth/account-inputBasic";
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") Account entity, Model model,
                                    @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        if (entity.isNew()) {
            Validation.isTrue(StringUtils.isNotBlank(rawPassword), "创建用户必须设置初始密码");
        }
        accountService.save(entity, rawPassword);
        return OperationResult.buildSuccessResult("数据保存处理完成", entity);
    }

    @RequiresPermissions("配置管理:权限管理:登录账户管理")
    @RequestMapping(value = "/logon-data", method = RequestMethod.GET)
    public String editShow(Model model) {
        return "admin/auth/account-logonData";
    }
}