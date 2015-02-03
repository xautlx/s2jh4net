package s2jh.biz.shop.support.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.support.service.VerifyCodeService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import s2jh.biz.shop.entity.SiteUser;
import s2jh.biz.shop.service.SiteUserService;

@Controller
@RequestMapping(value = "/w")
public class SiteIndexController extends BaseController<SiteUser, Long> {

    private final Logger logger = LoggerFactory.getLogger(SiteIndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    protected BaseService<SiteUser, Long> getEntityService() {
        return siteUserService;
    }

    @Override
    protected SiteUser buildBindingEntity() {
        return new SiteUser();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String wwwIndex(Model model) {
        return "w/index";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(Model model) {
        return "w/password-reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult passwordResetSmsValidate(HttpServletRequest request, SiteUser entity, Model model, @RequestParam("mobile") String mobile,
            @RequestParam("smsCode") String smsCode, @RequestParam(value = "newpasswd", required = false) String newpasswd) {
        if (verifyCodeService.verifySmsCode(request, mobile, smsCode)) {
            User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, mobile);
            if (user == null) {
                return OperationResult.buildFailureResult("号码尚未注册", "NoUser");
            }
            if (StringUtils.isBlank(newpasswd)) {
                return OperationResult.buildSuccessResult("短信验证码校验成功", "SmsOK");
            } else {
                //更新密码失效日期为6个月后
                user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
                userService.save(user, newpasswd);
                return OperationResult.buildSuccessResult("密码重置成功，您可以马上使用新设定密码登录系统啦", "ResetOK");
            }
        } else {
            return OperationResult.buildFailureResult("短信验证码不正确");
        }
    }
}
