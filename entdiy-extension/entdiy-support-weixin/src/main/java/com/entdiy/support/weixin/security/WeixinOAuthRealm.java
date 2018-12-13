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
import com.entdiy.auth.service.OauthAccountService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.support.weixin.service.WxOAuthAccountService;
import lombok.Setter;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class WeixinOAuthRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(WeixinOAuthRealm.class);

    public static final String ROLE_OAUTH_USER = "ROLE_OAUTH_USER";

    @Setter
    private WxOAuthAccountService wxOAuthAccountService;

    @Setter
    private OauthAccountService oauthAccountService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WeixinOAuthToken;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        WeixinOAuthToken token = (WeixinOAuthToken) authcToken;
        String username = token.getUsername();
        logger.debug("WeixinOAuthRealm process for: {}", username);


        Account.AuthTypeEnum authType = token.getAuthType();
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = token.getWxMpOAuth2AccessToken();
        WxMpUser wxMpUser = token.getWxMpUser();
        OauthAccount oauthAccount = oauthAccountService.findByOauthOpenIdAndOauthTypeAndAuthType(
                username, GlobalConstant.OauthTypeEnum.WECHAT, authType);

        //对于前端微信登录用户做必要的数据初始化和更新处理
        if (Account.AuthTypeEnum.site.equals(authType)) {
            if (oauthAccount == null) {
                oauthAccount = new OauthAccount();
                oauthAccount.setOauthType(GlobalConstant.OauthTypeEnum.WECHAT);
                oauthAccount.setOauthOpenId(username);
                oauthAccount.setAuthType(Account.AuthTypeEnum.site);
                oauthAccount.setBindTime(LocalDateTime.now());

                OauthAccount.OauthAccessToken oauthAccessToken = new OauthAccount.OauthAccessToken();
                BeanUtils.copyProperties(wxMpOAuth2AccessToken, oauthAccessToken);
                oauthAccount.setOauthAccessToken(oauthAccessToken);
            }
            if (wxMpUser != null) {
                OauthAccount.OauthUserinfo oauthUserinfo = oauthAccount.getOauthUserinfo();
                if (oauthUserinfo == null) {
                    oauthUserinfo = new OauthAccount.OauthUserinfo();
                    oauthAccount.setOauthUserinfo(oauthUserinfo);
                }
                BeanUtils.copyProperties(wxMpUser, oauthUserinfo);
            }
            wxOAuthAccountService.saveCasecadeAccount(oauthAccount);
        }

        Account account = oauthAccount.getAccount();
        //构造权限框架认证用户信息对象
        DefaultAuthUserDetails authUserDetails = new DefaultAuthUserDetails();
        authUserDetails.setDataDomain(account.getDataDomain());
        authUserDetails.setAccountId(account.getId());
        authUserDetails.setUsername(username);
        authUserDetails.setNickname(account.getNickname());
        authUserDetails.setAccessToken(account.getAccessToken());
        authUserDetails.setOauthType(GlobalConstant.OauthTypeEnum.WECHAT);
        authUserDetails.setOauthOpenId(username);

        return new SimpleAuthenticationInfo(authUserDetails, username, "Weixin OAuth Realm");
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(ROLE_OAUTH_USER);
        return info;
    }
}
