package lab.s2jh.support.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.security.PasswordService;
import lab.s2jh.core.security.ShiroJdbcRealm;
import lab.s2jh.core.service.GlobalConfigService;
import lab.s2jh.core.web.captcha.ImageCaptchaServlet;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.module.sys.service.MenuService;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.module.sys.service.UserMessageService;
import lab.s2jh.module.sys.vo.NavMenuVO;
import lab.s2jh.support.service.DynamicConfigService;
import lab.s2jh.support.service.MailService;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private MailService mailService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired(required = false)
    private ShiroJdbcRealm shiroJdbcRealm;

    @Autowired(required = false)
    private AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor;

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
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

        Subject subject = SecurityUtils.getSubject();
        //先从session中获取缓存的菜单数据
        @SuppressWarnings("unchecked")
        List<NavMenuVO> userNavMenuVOs = (List<NavMenuVO>) session.getAttribute("user.menus");
        if (userNavMenuVOs == null) {
            userNavMenuVOs = Lists.newArrayList();
        } else {
            //如果是生产环境，并且已有菜单数据，则直接返回
            if (!GlobalConfigService.isDevMode()) {
                return userNavMenuVOs;
            }
        }

        logger.debug("Build user menu list according to privileges for: {}", subject.getPrincipal());

        //获取所有有效的菜单集合
        List<NavMenuVO> navMenuVOs = menuService.findAvailableNavMenuVOs();

        if (logger.isDebugEnabled()) {
            AuthorizationInfo authorizationInfo = shiroJdbcRealm.getAuthorizationInfo(subject.getPrincipals());
            Collection<String> roles = authorizationInfo.getRoles();
            logger.debug("Role list: ");
            for (String role : roles) {
                logger.debug(" - {}", role);
            }

            Collection<String> permissions = authorizationInfo.getStringPermissions();
            logger.debug("Permission list:");
            if (permissions != null) {
                for (String permission : permissions) {
                    logger.debug(" - {}", permission);
                }
            }
        }
        //计算用户有访问权限的菜单列表
        AopAllianceAnnotationsAuthorizingMethodInterceptor shiroMethodInterceptor = (AopAllianceAnnotationsAuthorizingMethodInterceptor) authorizationAttributeSourceAdvisor
                .getAdvice();
        Collection<AuthorizingAnnotationMethodInterceptor> aamis = shiroMethodInterceptor.getMethodInterceptors();
        for (NavMenuVO navMenuVO : navMenuVOs) {
            MethodInvocation mi = menuService.getMappedMethodInvocation(navMenuVO);
            if (mi != null) {
                try {
                    //每个菜单项对应一个Controller方法调用，直接调用shiro的拦截器校验逻辑判断登录用户是否有方法访问权限，从而确定菜单是否显示
                    assertAuthorized(aamis, mi);
                } catch (AuthorizationException e) {
                    //没有菜单对应方法访问权限，则跳过此菜单项
                    continue;
                }
            }
            //添加用户显示菜单项
            userNavMenuVOs.add(navMenuVO);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Menu list:");
            for (NavMenuVO navMenuVO : userNavMenuVOs) {
                logger.debug(" - {}", navMenuVO.getPath());
            }
        }

        return userNavMenuVOs;
    }

    /**
     * 参考 @see AnnotationsAuthorizingMethodInterceptor.assertAuthorized 对应protected方法实现一个当前私有的校验逻辑
     */
    private void assertAuthorized(Collection<AuthorizingAnnotationMethodInterceptor> aamis, MethodInvocation methodInvocation)
            throws AuthorizationException {
        //default implementation just ensures no deny votes are cast:
        if (aamis != null && !aamis.isEmpty()) {
            for (AuthorizingAnnotationMethodInterceptor aami : aamis) {
                if (aami.supports(methodInvocation)) {
                    aami.assertAuthorized(methodInvocation);
                }
            }
        }
    }

    @RequestMapping(value = "/admin/password/forget", method = RequestMethod.GET)
    public String forgetPasswordShow(Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/pub/password-forget";
    }

    @RequestMapping(value = "/admin/password/forget", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult forgetPasswordSave(HttpServletRequest request, @RequestParam("uid") String uid, @RequestParam("captcha") String captcha) {
        if (!ImageCaptchaServlet.validateResponse(request, captcha)) {
            return OperationResult.buildFailureResult("验证码不正确，请重新输入");
        }
        User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, uid);
        if (user == null) {
            user = userService.findByProperty("email", uid);
        }
        if (user == null) {
            return OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理");
        }
        String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            return OperationResult.buildFailureResult("当前账号未设定注册邮箱，请联系管理员先设置邮箱后再进行此操作");
        } else {

            int serverPort = request.getServerPort();
            // Reconstruct original requesting URL
            StringBuffer url = new StringBuffer();
            url.append(request.getScheme()).append("://").append(request.getServerName());
            if ((serverPort != 80) && (serverPort != 443)) {
                url.append(":").append(serverPort);
            }
            String contextPath = request.getContextPath();
            if (!"/".equals(contextPath)) {
                url.append(contextPath);
            }

            userService.requestResetPassword(url, user);
        }
        return OperationResult.buildSuccessResult("找回密码请求处理成功。重置密码邮件已发送至：" + email);
    }

    @RequestMapping(value = "/admin/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(Model model) {
        return "admin/pub/password-reset";
    }

    @RequestMapping(value = "/admin/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult resetPasswordSave(HttpServletRequest request, HttpServletResponse response, @RequestParam("uid") String uid,
            @RequestParam("code") String code, @RequestParam("newpasswd") String newpasswd, RedirectAttributes redirectAttributes) throws IOException {
        User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, uid);
        if (user != null) {
            if (code.equals(user.getRandomCode())) {
                //user.setRandomCode(null);
                //更新密码失效日期为6个月后
                user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
                userService.save(user, newpasswd);
                return OperationResult.buildSuccessResult("密码重置成功，您可以马上使用新设定密码登录系统啦").setRedirect("/admin/login");
            } else {
                return OperationResult.buildFailureResult("验证码不正确或已失效，请尝试重新找回密码操作");
            }
        }
        return OperationResult.buildFailureResult("操作失败");
    }

    @RequestMapping(value = "/admin/signup", method = RequestMethod.GET)
    public String signupShow(Model model) {
        model.addAttribute("mailServiceEnabled", mailService.isEnabled());
        return "admin/pub/signup";
    }

    @RequestMapping(value = "/admin/signup", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult signupSave(HttpServletRequest request, @RequestParam("captcha") String captcha) {
        if (!ImageCaptchaServlet.validateResponse(request, captcha)) {
            return OperationResult.buildFailureResult("验证码不正确，请重新输入");
        }
        if (dynamicConfigService.getBoolean("cfg_signup_disabled", false)) {
            return OperationResult.buildFailureResult("系统暂未开发账号注册功能，如有疑问请联系管理员");
        }
        //TODO 
        return OperationResult.buildSuccessResult("注册成功。需要等待管理员审批通过后方可登录系统。");
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
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web-admin", null);
        } else {
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web-admin",
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
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(NotifyMessage.class, request);
        groupFilter.append(new PropertyFilter(MatchType.EQ, "targetUser", user));
        groupFilter.append(new PropertyFilter(MatchType.EQ, "effective", Boolean.TRUE));
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
        model.addAttribute("notifyMessage", userMessage);
        return "admin/profile/notifyMessage-view";
    }
}
