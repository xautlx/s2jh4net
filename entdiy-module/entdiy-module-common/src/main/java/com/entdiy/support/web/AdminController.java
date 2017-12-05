package com.entdiy.support.web;

import com.entdiy.auth.entity.User;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.security.AuthContextHolder;
import com.entdiy.security.AuthUserDetails;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.UserMessage;
import com.entdiy.sys.service.MenuService;
import com.entdiy.sys.service.NotifyMessageService;
import com.entdiy.sys.service.UserMessageService;
import com.entdiy.sys.vo.NavMenuVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserMessageService userMessageService;


    @Autowired(required = false)
    private AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor;

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        model.addAttribute("devMode", GlobalConfigService.isDevMode());
        return "admin/dashboard";
    }

    /**
     * 计算显示用户登录菜单数据
     */
    @RequestMapping(value = "/admin/menus", method = RequestMethod.GET)
    @ResponseBody
    public List<NavMenuVO> navMenu(HttpSession session) {
        User user = AuthContextHolder.findAuthUser();
        //如果未登录则直接返回空
        if (user == null) {
            return Lists.newArrayList();
        }
        return menuService.processUserMenu(user);
    }

    @MenuData("个人信息:公告消息")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message", method = RequestMethod.GET)
    public String notifyMessageIndex() {
        return "admin/profile/notifyMessage-index";
    }

    @MetaData("公告消息列表")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message-list", method = RequestMethod.GET)
    public String notifyMessageList(HttpServletRequest request, Model model) {
        User user = AuthContextHolder.findAuthUser();
        List<NotifyMessage> notifyMessages = null;
        String readed = request.getParameter("readed");
        if (StringUtils.isBlank(readed)) {
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web_admin", null);
        } else {
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web_admin",
                    BooleanUtils.toBoolean(request.getParameter("readed")));
        }
        model.addAttribute("notifyMessages", notifyMessages);
        return "admin/profile/notifyMessage-list";
    }

    @MetaData("公告消息读取")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message-view/{messageId}", method = RequestMethod.GET)
    public String notifyMessageView(@PathVariable("messageId") Long messageId, Model model) {
        User user = AuthContextHolder.findAuthUser();
        NotifyMessage notifyMessage = notifyMessageService.findOne(messageId);
        notifyMessageService.processUserRead(notifyMessage, user);
        model.addAttribute("notifyMessage", notifyMessage);
        return "admin/profile/notifyMessage-view";
    }

    @MenuData("个人信息:个人消息")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/user-message", method = RequestMethod.GET)
    public String userMessageIndex() {
        return "admin/profile/userMessage-index";
    }

    @MetaData("个人消息列表")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/user-message-list", method = RequestMethod.GET)
    public String userMessageList(HttpServletRequest request, Model model) {
        User user = AuthContextHolder.findAuthUser();
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(UserMessage.class, request);
        groupFilter.append(new PropertyFilter(MatchType.EQ, "targetUser", user));
        String readed = request.getParameter("readed");
        if (StringUtils.isNotBlank(readed)) {
            if (BooleanUtils.toBoolean(request.getParameter("readed"))) {
                groupFilter.append(new PropertyFilter(MatchType.NN, "firstReadTime", Boolean.TRUE));
            } else {
                groupFilter.append(new PropertyFilter(MatchType.NU, "firstReadTime", Boolean.TRUE));
            }
        }
        Page<UserMessage> pageData = userMessageService.findByPage(groupFilter, pageable);
        model.addAttribute("pageData", pageData);
        return "admin/profile/userMessage-list";
    }

    @MetaData("个人消息读取")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/user-message-view/{messageId}", method = RequestMethod.GET)
    public String userMessageView(@PathVariable("messageId") Long messageId, Model model) {
        User user = AuthContextHolder.findAuthUser();
        UserMessage userMessage = userMessageService.findOne(messageId);
        userMessageService.processUserRead(userMessage, user);
        model.addAttribute("userMessage", userMessage);
        return "admin/profile/userMessage-view";
    }
}
