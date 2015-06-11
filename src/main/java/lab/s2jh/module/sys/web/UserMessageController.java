package lab.s2jh.module.sys.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.module.sys.service.UserMessageService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/sys/user-message")
public class UserMessageController extends BaseController<UserMessage, Long> {

    @Autowired
    private UserMessageService userMessageService;

    @Override
    protected BaseService<UserMessage, Long> getEntityService() {
        return userMessageService;
    }

    @MenuData("配置管理:系统管理:消息管理")
    @RequiresPermissions("配置管理:系统管理:消息管理")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/sys/userMessage-index";
    }

    @RequiresPermissions("配置管理:系统管理:消息管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserMessage> findByPage(HttpServletRequest request) {
        return super.findByPage(UserMessage.class, request);
    }
}
