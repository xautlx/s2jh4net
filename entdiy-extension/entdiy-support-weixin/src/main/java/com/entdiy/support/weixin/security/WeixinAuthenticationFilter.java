/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
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
package com.entdiy.support.weixin.security;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.entity.OauthAccount;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.OauthAccountService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.support.weixin.service.WxMpService;
import lombok.Setter;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

public class WeixinAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(WeixinAuthenticationFilter.class);

    @Setter
    private WxMpService wxMpService;

    @Setter
    private OauthAccountService oauthAccountService;

    @Setter
    private AccountService accountService;

    @Setter
    private String oath2State;

    @Setter
    private String oauth2Scope = WxConsts.OAuth2Scope.SNSAPI_USERINFO;

    /**
     * 详见：https://github.com/HADB/GetWeixinCode
     */
    @Setter
    private String authorizationProxyUrl;

    protected AuthenticationToken createToken(WxMpOAuth2AccessToken wxMpOAuth2AccessToken, WxMpUser wxMpUser, ServletRequest request, ServletResponse response) {
        String host = getHost(request);
        Account.AuthTypeEnum authType = Account.AuthTypeEnum.site;
        String authTypeParam = request.getParameter("authType");
        if (StringUtils.isNotBlank(authTypeParam)) {
            authType = Account.AuthTypeEnum.valueOf(authTypeParam);
        }
        return new WeixinOAuthToken(authType, wxMpOAuth2AccessToken, wxMpUser, true, host);
    }

    public String oauth2buildAuthorizationUrl(String authorizationUrl, String oauth2Scope, String oath2State) {
        //https://github.com/HADB/GetWeixinCode
        //如果配置中转url与当前应用上下文不一致，则采用中转认证URL，否则按照标准的转向认证方式
        if (StringUtils.isNotBlank(authorizationProxyUrl) && authorizationProxyUrl.indexOf(AppContextHolder.getWebContextUri()) <= -1) {
            //http://www.abc.com/xxx/get-weixin-code.html?appid=XXXX&scope=snsapi_base&state=hello-world&redirect_uri=http%3A%2F%2Fwww.xyz.com%2Fhello-world.html
            StringBuilder proxyUrl = new StringBuilder(authorizationProxyUrl);
            proxyUrl.append("?appid=" + wxMpService.getWxMpConfigStorage().getAppId());
            proxyUrl.append("&scope=" + oauth2Scope);
            proxyUrl.append("&state=" + oath2State);
            proxyUrl.append("&redirect_uri=" + URIUtil.encodeURIComponent(authorizationUrl));
            authorizationUrl = proxyUrl.toString();
        }
        logger.debug("Using authorizationUrl: {}", authorizationUrl);

        return wxMpService.oauth2buildAuthorizationUrl(authorizationUrl, oauth2Scope, oath2State);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            return executeLogin(request, response);
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " + "Authentication url ["
                        + getLoginUrl() + "]");
            }

            saveRequest(request);

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String query = httpServletRequest.getQueryString();
            if (query == null) {
                query = "";
            } else {
                query = "?" + StringUtils.substringBefore(query, "forceLogin=true") + StringUtils.substringAfter(query, "forceLogin=true");
            }
            //开发模拟openid登录支持
            if (getMockOAuth2AccessToken(request) != null) {
                String redirectURL = "/wx/authorize" + query;
                logger.debug("redirect to: {}", redirectURL);
                WebUtils.issueRedirect(request, response, redirectURL);
            } else {
                String authorizationUrl = AppContextHolder.getWebContextUri() + "/wx/authorize" + query;
                String redirectURL = oauth2buildAuthorizationUrl(authorizationUrl, oauth2Scope, oath2State);
                logger.debug("redirect to: {}", redirectURL);
                WebUtils.issueRedirect(request, response, redirectURL, null, false, true);
            }
            return false;
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        WxMpUser wxMpUser = null;
        String code = request.getParameter("code");
        if (StringUtils.isNotBlank(code)) {
            logger.info("Weixin oauth by code={}", code);
            try {
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
                logger.debug("wxMpOAuth2AccessToken: {}" + wxMpOAuth2AccessToken);


                if (WxConsts.OAuth2Scope.SNSAPI_USERINFO.equalsIgnoreCase(oauth2Scope)) {
                    wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
                    logger.debug("wxMpUser: {}" + wxMpUser);
                }
            } catch (WxErrorException e) {
                throw new AuthenticationException(e);
            }
        } else {
            //开发模拟openid登录支持
            wxMpOAuth2AccessToken = getMockOAuth2AccessToken(httpServletRequest);
            wxMpUser = new WxMpUser();
            wxMpUser.setNickname("Mock User");
        }

        if (wxMpOAuth2AccessToken == null) {
            return true;
        }

        Subject subject = getSubject(request, response);

        WeixinOAuthToken weixinOAuthToken = (WeixinOAuthToken) createToken(wxMpOAuth2AccessToken, wxMpUser, request, response);
        OauthAccount oauthAccount = oauthAccountService.findByOauthOpenIdAndOauthTypeAndAuthType(
                weixinOAuthToken.getUsername(), GlobalConstant.OauthTypeEnum.WECHAT, weixinOAuthToken.getAuthType());
        if (weixinOAuthToken.getAuthType().equals(Account.AuthTypeEnum.admin)) {
            if (oauthAccount == null) {
                String msg = "请先使用账号密码登录后台系统进行微信绑定后，才可直接使用微信登录后台系统";
                String url = "/admin/login?error=" + URLEncoder.encode(msg, "UTF-8");
                WebUtils.issueRedirect(request, response, url, null, true);
                return false;
            }
        }

        subject.login(weixinOAuthToken);
        issueSuccessRedirect(request, response);
        return false;
    }

    private WxMpOAuth2AccessToken getMockOAuth2AccessToken(ServletRequest request) {
        //开发演示模式才支持模拟访问
        if (AppContextHolder.isDevMode() || AppContextHolder.isDemoMode()) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            //非微信模式访问才支持模拟访问
            if (!ServletUtils.isMicroMessengerClient(httpServletRequest)) {
                String openId = request.getParameter("mock");
                if (StringUtils.isNotBlank(openId)) {
                    logger.debug("Using weixin mock openId={}", openId);
                    WxMpOAuth2AccessToken mock = new WxMpOAuth2AccessToken();
                    mock.setOpenId(openId);
                    return mock;
                }
            }
        }
        return null;
    }
}
