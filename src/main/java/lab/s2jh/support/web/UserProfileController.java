package lab.s2jh.support.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.support.service.MailService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private MailService mailService;

    @MetaData("用户未读消息数目")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message-count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult notifyMessageCount(@ModelAttribute("user") User user) {
        return OperationResult.buildSuccessResult(notifyMessageService.findMgmtCountToRead(user));
    }

    @MenuData("个人信息:通知消息")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message", method = RequestMethod.GET)
    public String notifyMessageIndex() {
        return "admin/profile/notifyMessage-index";
    }

    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message-list", method = RequestMethod.GET)
    public String notifyMessageList(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        Map<String, Object> searchParams = ServletUtils.buildParameters(request);
        String publishTime = (String) searchParams.get("publishTime");
        if (StringUtils.isNotBlank(publishTime)) {
            searchParams.put("publishTimeFrom", publishTime + " 00:00:00");
            searchParams.put("publishTimeTo", publishTime + " 23:59:59");
        }
        Page<NotifyMessage> pageData = notifyMessageService.findMgmtPageToRead(user, searchParams,
                PropertyFilter.buildPageableFromHttpRequest(request));
        model.addAttribute("pageData", pageData);
        return "admin/profile/notifyMessage-list";
    }

    @MetaData("用户消息读取")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/notify-message-view/{messageId}", method = RequestMethod.GET)
    public String notifyMessageView(@ModelAttribute("user") User user, @PathVariable("messageId") Long messageId, Model model) {
        NotifyMessage notifyMessage = notifyMessageService.findOne(messageId);
        //数据访问权限检查
        Assert.isTrue((notifyMessage.getMgmtShow() && notifyMessage.getTargetUser() == null)
                || notifyMessage.getTargetUser().getId().equals(user.getId()));
        notifyMessageService.processUserRead(notifyMessage, user);
        model.addAttribute("notifyMessage", notifyMessage);
        return "admin/profile/notifyMessage-view";
    }

    @MenuData("个人信息:个人配置")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/edit", method = RequestMethod.GET)
    public String profileEditShow() {
        return "admin/profile/profile-edit";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/password", method = RequestMethod.GET)
    public String modifyPasswordShow(Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/profile/password-edit";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/password", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult modifyPasswordSave(HttpServletRequest request, @RequestParam("oldpasswd") String oldpasswd,
            @RequestParam("newpasswd") String newpasswd) {
        User user = userService.findAuthUser();
        String encodedPasswd = userService.encodeUserPasswd(user, oldpasswd);
        if (!encodedPasswd.equals(user.getPassword())) {
            return OperationResult.buildFailureResult("原密码不正确,请重新输入");
        } else {
            //更新密码失效日期为6个月后
            user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
            userService.save(user, newpasswd);
            return OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码");
        }
    }

    @RequiresUser
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @ModelAttribute
    public void prepareModel(Model model) {
        model.addAttribute("user", userService.findAuthUser());
    }
}
