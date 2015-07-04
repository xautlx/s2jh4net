package lab.s2jh.support.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.sys.entity.NotifyMessage.NotifyMessagePlatformEnum;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.module.sys.service.SmsVerifyCodeService;
import lab.s2jh.module.sys.service.UserMessageService;
import lab.s2jh.module.sys.service.UserProfileDataService;
import lab.s2jh.support.service.DynamicConfigService;
import lab.s2jh.support.service.SmsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsVerifyCodeService smsVerifyCodeService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserProfileDataService userProfileDataService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String defaultIndex() {
        return "redirect:/w";
    }

    @RequestMapping(value = "/m", method = RequestMethod.GET)
    public String mobileIndex() {
        return "m/index";
    }

    @RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
    public String unauthorizedUrl(HttpServletRequest request, Model model) {
        model.addAttribute("readFileUrlPrefix", ServletUtils.getReadFileUrlPrefix());
        return "error/403";
    }

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminIndex(HttpServletRequest request, Model model) {
        model.addAttribute("buildVersion", dynamicConfigService.getString("build_version"));
        User user = AuthContextHolder.findAuthUser();
        model.addAttribute("layoutAttributes", userProfileDataService.findMapDataByUser(user));
        model.addAttribute("readFileUrlPrefix", ServletUtils.getReadFileUrlPrefix());
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
     * <h3>APP接口: 登录。</h3>
     * 
     * <p>
     * 业务输入参数列表：
     * <ul> 
     * <li><b>username</b> 账号</li>
     * <li><b>password</b> 密码</li>
     * <li><b>uuid</b> 设备或应用唯一标识</li>
     * </ul> 
     * </p>
     * 
     * <p>
     * 业务输出参数列表：
     * <ul>
     * <li><b>token</b> 本次登录的随机令牌Token，目前设定半年有效期。APP取到此token值后存储在本应用持久化，在后续访问或下次重开应用时把此token以HTTP Header形式附在Request信息中：ACCESS-TOKEN={token}</li>
     * </ul>
     * </p>
     * 
     * @return  {@link OperationResult} 通用标准结构
     * 
     */
    @RequestMapping(value = "/app/login", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult appLogin(HttpServletRequest request, Model model) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae == null) {
            Map<String, Object> datas = Maps.newHashMap();
            datas.put("token", AuthContextHolder.getAuthUserDetails().getAccessToken());
            return OperationResult.buildSuccessResult(datas);
        } else {
            return OperationResult.buildFailureResult(ae.getMessage());
        }
    }

    /**
     * PC站点方式登录失败，转向登录界面。表单的/login POST请求首先会被Shiro拦截处理，在认证失败之后才会触发调用此方法
     * @param source 登录来源,  @see SourceUsernamePasswordToken.AuthSourceEnum
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{source}/login", method = RequestMethod.POST)
    public String loginFailure(@PathVariable("source") String source, HttpServletRequest request, Model model) {
        //获取认证异常的类名
        AuthenticationException ae = (AuthenticationException) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (ae != null) {
            model.addAttribute("error", ae.getMessage());
            return source + "/login";
        } else {
            return "redirect:/" + source;
        }

    }

    @RequestMapping(value = "/send-sms-code/{mobile}", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult sendSmsCode(@PathVariable("mobile") String mobile, HttpServletRequest request) {
        String code = smsVerifyCodeService.generateSmsCode(request, mobile);
        String msg = "您的操作验证码为：" + code + "。【请勿向任何人提供您收到的短信验证码】。如非本人操作，请忽略本信息。";
        if (smsService.sendSMS(msg, mobile)) {
            return OperationResult.buildSuccessResult();
        } else {
            return OperationResult.buildFailureResult("短信发送失败");
        }
    }

    /**
     * 
     * @param platform 平台
     * @return
     */
    @MetaData("用户未读公告数目")
    @RequestMapping(value = "/notify-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult notifyMessageCount(HttpServletRequest request) {
        User user = AuthContextHolder.findAuthUser();
        String platform = request.getParameter("platform");
        if (StringUtils.isBlank(platform)) {
            platform = NotifyMessagePlatformEnum.web_admin.name();
        }

        return OperationResult.buildSuccessResult(notifyMessageService.findCountToRead(user, platform));
    }

    @MetaData("用户未读消息数目")
    @RequestMapping(value = "/user-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult userMessageCount() {
        User user = AuthContextHolder.findAuthUser();
        if (user != null) {
            return OperationResult.buildSuccessResult(userMessageService.findCountToRead(user));
        } else {
            return OperationResult.buildSuccessResult(0);
        }
    }
}
