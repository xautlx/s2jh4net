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
package com.entdiy.security.api;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.security.DefaultAuthUserDetails;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class BearerTokenRealm extends AuthorizingRealm {

    @Setter
    private AccountService accountService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BearerAuthenticationToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        BearerAuthenticationToken bearerToken = (BearerAuthenticationToken) authcToken;

        String accessToken = bearerToken.getAccessToken();

        Account account = accountService.findByAccessToken(accessToken);
        if (account == null) {
            throw new UnknownAccountException("登录信息不正确");
        }

        //构造权限框架认证用户信息对象
        DefaultAuthUserDetails authUserDetails = new DefaultAuthUserDetails();
        authUserDetails.setUsername(account.getAuthUid());
        authUserDetails.setNickname(account.getNickname());
        authUserDetails.setAccountId(account.getId());
        authUserDetails.setDataDomain(account.getDataDomain());
        authUserDetails.setAccessToken(accessToken);

        return new SimpleAuthenticationInfo(authUserDetails, accessToken, "Bearer Token Realm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}
