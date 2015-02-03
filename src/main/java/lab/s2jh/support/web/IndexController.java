package lab.s2jh.support.web;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.security.JcaptchaFormAuthenticationFilter.AuthenticationValidationException;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.support.service.DynamicConfigService;
import lab.s2jh.support.service.SmsService;
import lab.s2jh.support.service.VerifyCodeService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String defaultIndex() {
        return "redirect:/w";
    }

    @RequestMapping(value = "/m", method = RequestMethod.GET)
    public String mobileIndex() {
        return "m/index";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminIndex() {
        return "admin/index";
    }

    @RequestMapping(value = "/{source}/login", method = RequestMethod.GET)
    public String adminLogin(Model model, @PathVariable("source") String source) {
        model.addAttribute("buildVersion", dynamicConfigService.getString("build_version"));
        //自助注册管理账号功能开关
        model.addAttribute("signupEnabled", !dynamicConfigService.getBoolean("cfg_mgmt_signup_disabled", true));
        return source + "/login";
    }

    /**
     * 表单的/login POST请求首先会被Shiro拦截处理，在认证失败之后才会触发调用此方法
     * @param source 登录来源,  @see SourceUsernamePasswordToken.AuthSourceEnum
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{source}/login", method = RequestMethod.POST)
    public String loginFailure(@PathVariable("source") String source, HttpServletRequest request, Model model) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae instanceof AuthenticationValidationException) {
            model.addAttribute("error", ae.getMessage());
        } else {
            model.addAttribute("error", "登录账号或密码不正确");
        }
        return source + "/login";
    }

    @RequestMapping(value = "/send-sms-code/{mobile}", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult sendSmsCode(@PathVariable("mobile") String mobile, HttpServletRequest request) {
        String code = verifyCodeService.generateSmsCode(request, mobile);
        String msg = "验证码：" + code;
        if (smsService.sendSMS(msg, mobile)) {
            return OperationResult.buildSuccessResult();
        } else {
            return OperationResult.buildFailureResult("短信发送失败");
        }
    }
}
