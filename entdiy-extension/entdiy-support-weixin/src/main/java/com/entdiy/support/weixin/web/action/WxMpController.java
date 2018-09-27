/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.support.weixin.web.action;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.entity.OauthAccount;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.OauthAccountService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.security.annotation.AuthAccount;
import com.entdiy.support.weixin.security.WeixinAuthenticationFilter;
import com.entdiy.support.weixin.service.WxMpService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/wx")
public class WxMpController {

    private final static Logger logger = LoggerFactory.getLogger(WxMpController.class);

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OauthAccountService oauthAccountService;

    @Autowired
    private WeixinAuthenticationFilter weixinAuthenticationFilter;

    @GetMapping("/echostr")
    public void wechatCore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        logger.debug("nonce={}", nonce);
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }
        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echoStr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            String echoStrOut = String.copyValueOf(echoStr.toCharArray());
            response.getWriter().println(echoStrOut);
            return;
        }
    }

    @GetMapping("/goto-app")
    public String weixinGotoApp(
            HttpServletRequest request,
            @RequestParam(value = "to", required = true) String to) {
        DefaultAuthUserDetails defaultAuthUserDetails = (DefaultAuthUserDetails) AuthContextHolder.getAuthUserDetails();
        String token = defaultAuthUserDetails.getAccessToken();

        //查询数据库，如果没有对象说明是重建数据库了，主要用在开发环境反复重建数据库场景，则做一个登录注销动作
        Account account = accountService.findByAccessToken(token);
        if (account == null) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return "redirect:" + to;
        }

        if (AppContextHolder.isDevMode() && !ServletUtils.isMicroMessengerClient(request)) {
            String mock = request.getParameter("mock");
            if (StringUtils.isNotBlank(mock) && !mock.equals(defaultAuthUserDetails.getUsername())) {
                OauthAccount oauthAccount = oauthAccountService.findByOauthOpenIdAndOauthType(mock, GlobalConstant.OauthTypeEnum.MOCK);
                if (oauthAccount != null) {
                    account = oauthAccount.getAccount();
                    token = account.getAccessToken();
                }
            }
        }

        StringBuilder redirectURL = new StringBuilder(to);
        if (to.indexOf("?") <= -1) {
            redirectURL.append("?token=" + token);
        } else {
            redirectURL.append("&token=" + token);
        }
        redirectURL.append("&buildVersion=" + AppContextHolder.getBuildVersion());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/admin-login")
    public String weixinGotoAdminLogin(HttpServletRequest request, Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/wx/goto-admin?authType=admin";
    }

    @GetMapping("/goto-admin")
    public String weixinGotoAdmin(HttpServletRequest request, Model model) {
        return "redirect:/admin";
    }

    @GetMapping("/goto-admin-bind")
    public String weixinGotoAdminBind(HttpServletRequest request, Model model) {
        String authorizationUrl = AppContextHolder.getWebContextUri() + "/wx/admin-bind";
        return "redirect:" + weixinAuthenticationFilter.oauth2buildAuthorizationUrl(authorizationUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, "bind");
    }

    @GetMapping("/admin-bind")
    public String weixinAdminBind(HttpServletRequest request, Model model) throws Exception {
        String code = request.getParameter("code");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);

        String openid = wxMpOAuth2AccessToken.getOpenId();
        OauthAccount oauthAccount = oauthAccountService.findByOauthOpenIdAndOauthTypeAndAuthType(
                openid, GlobalConstant.OauthTypeEnum.WECHAT, Account.AuthTypeEnum.admin);
        if (oauthAccount == null) {
            oauthAccount = new OauthAccount();
            oauthAccount.setOauthType(GlobalConstant.OauthTypeEnum.WECHAT);
            oauthAccount.setOauthOpenId(openid);
            oauthAccount.setAuthType(Account.AuthTypeEnum.admin);
            oauthAccount.setBindTime(LocalDateTime.now());

            OauthAccount.OauthAccessToken oauthAccessToken = new OauthAccount.OauthAccessToken();
            BeanUtils.copyProperties(wxMpOAuth2AccessToken, oauthAccessToken);
            oauthAccount.setOauthAccessToken(oauthAccessToken);

            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            OauthAccount.OauthUserinfo oauthUserinfo = new OauthAccount.OauthUserinfo();
            BeanUtils.copyProperties(wxMpUser, oauthUserinfo);
            oauthAccount.setOauthUserinfo(oauthUserinfo);

            AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
            Account account = accountService.findOne(authUserDetails.getAccountId());
            oauthAccount.setAccount(account);

            oauthAccountService.save(oauthAccount);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin-unbind")
    @ResponseBody
    public OperationResult weixinAdminUnBind(@AuthAccount Account account, String openid) {
        OauthAccount oauthAccount = oauthAccountService.findByAccountAndOauthOpenIdAndOauthTypeAndAuthType(
                account, openid, GlobalConstant.OauthTypeEnum.WECHAT, Account.AuthTypeEnum.admin);
        oauthAccountService.delete(oauthAccount);
        return OperationResult.buildSuccessResult("微信解绑操作成功");
    }

    @GetMapping("/weixin-access-required")
    public String weixinAccessRequired() {
        return "/wx/weixinAccessRequired";
    }

    @GetMapping("/jsapi-signture")
    @ResponseBody
    public OperationResult weixinJsapiSignture(@RequestParam("url") String url) throws WxErrorException {
        try {
            WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
            return OperationResult.buildSuccessResult(wxJsapiSignature);
        } catch (WxErrorException e) {
            logger.error("weixinJsapiSignture error", e);
            return OperationResult.buildFailureResult("微信接口异常，请联系客服处理");
        }
    }
}
