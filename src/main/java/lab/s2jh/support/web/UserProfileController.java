package lab.s2jh.support.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.Validation;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.module.sys.service.UserMessageService;
import lab.s2jh.support.service.MailService;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private UserMessageService userMessageService;

    @Autowired
    private MailService mailService;

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
        User user = AuthContextHolder.findAuthUser();
        String encodedPasswd = userService.encodeUserPasswd(user, oldpasswd);
        if (!encodedPasswd.equals(user.getPassword())) {
            return OperationResult.buildFailureResult("原密码不正确,请重新输入");
        } else {
            Validation.notDemoMode();
            //更新密码失效日期为6个月后
            user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
            userService.save(user, newpasswd);
            return OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码");
        }
    }

    @MetaData("密码过期强制重置-显示")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/credentials-expire", method = RequestMethod.GET)
    public String profileCredentialsExpireShow() {
        return "admin/profile/credentials-expire";
    }

    @MetaData("密码过期强制重置-更新")
    @RequiresRoles(value = AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/profile/credentials-expire", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult profileCredentialsExpireSave(@RequestParam("newpasswd") String newpasswd) {
        User user = AuthContextHolder.findAuthUser();
        Validation.notDemoMode();
        //更新密码失效日期为6个月后
        user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
        userService.save(user, newpasswd);
        return OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码").setRedirect("/admin");
    }

    @ModelAttribute
    public void prepareModel(Model model) {
        model.addAttribute("user", AuthContextHolder.findAuthUser());
    }
}
