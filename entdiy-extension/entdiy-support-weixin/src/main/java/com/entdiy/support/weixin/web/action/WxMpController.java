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
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.support.weixin.service.WxMpService;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/wx")
public class WxMpController {

    private final static Logger logger = LoggerFactory.getLogger(WxMpController.class);

    @Value("${user.app.uri}")
    private String userAppUri;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OauthAccountService oauthAccountService;

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
            @RequestParam(value = "to", required = false) String to) {
        DefaultAuthUserDetails defaultAuthUserDetails = (DefaultAuthUserDetails) AuthContextHolder.getAuthUserDetails();
        String token = defaultAuthUserDetails.getAccessToken();
        if (AppContextHolder.isDevMode()) {
            String mock = request.getParameter("mock");
            if (StringUtils.isNotBlank(mock) && !mock.equals(defaultAuthUserDetails.getUsername())) {
                OauthAccount oauthAccount = oauthAccountService.findByOauthTypeAndOauthOpenId(OauthAccount.OauthTypeEnum.MOCK, mock);
                if (oauthAccount != null) {
                    Account account = oauthAccount.getAccount();
                    token = account.getAccessToken();
                }
            }
        }

        String loginUrl = userAppUri + "/login？";
        String url = StringUtils.isNotBlank(to) ? loginUrl + "to=" + to : loginUrl;
        StringBuilder redirectURL = new StringBuilder(url);
        redirectURL.append("&token=" + token);
        redirectURL.append("&buildVersion=" + AppContextHolder.getBuildVersion());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/weixin-access-required")
    public String weixinAccessRequired() {
        return "/wx/weixinAccessRequired";
    }

    @GetMapping("/jsapi-signture")
    @ResponseBody
    public OperationResult weixinJsapiSignture(@RequestParam("url") String url) throws WxErrorException {
        WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
        return OperationResult.buildSuccessResult(wxJsapiSignature);
    }
}
