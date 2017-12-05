package com.entdiy.aud.web;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.aud.entity.UserLogonLog;
import com.entdiy.aud.service.UserLogonLogService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.json.JsonViews;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@MetaData("配置管理:系统记录:账户登录记录管理")
@Controller
@RequestMapping(value = "/admin/aud/user-logon-log")
public class UserLogonLogController extends BaseController<UserLogonLog, Long> {

    @Autowired
    private UserLogonLogService userLogonLogService;

    @Override
    protected BaseService<UserLogonLog, Long> getEntityService() {
        return userLogonLogService;
    }

    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }

    @MenuData("配置管理:系统记录:账户登录记录")
    @RequiresPermissions("配置管理:系统记录:账户登录记录")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/aud/userLogonLog-index";
    }

    @RequiresPermissions("配置管理:系统记录:账户登录记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<UserLogonLog> findByPage(HttpServletRequest request) {
        return super.findByPage(UserLogonLog.class, request);
    }

    @RequiresPermissions("配置管理:系统记录:账户登录记录")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        return "admin/aud/userLogonLog-inputBasic";
    }
}