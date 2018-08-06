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
package com.entdiy.auth.service;

import com.entdiy.auth.dao.AccountDao;
import com.entdiy.auth.entity.Account;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.UidUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.security.PasswordService;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.support.service.FreemarkerService;
import com.entdiy.support.service.MailService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

@Service
@Transactional
public class AccountService extends BaseService<Account, Long> {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private MailService mailService;

    @Autowired(required = false)
    private FreemarkerService freemarkerService;

    /**
     * 基于登录界面输入的单一username参数值进行转换处理查询用户对象
     *
     * @param username 登录名称，支持域账户形式(如 admin\\user1, admin\\user2 )，前面部分会转换为dataDomain属性匹配
     * @return
     */
    public Account findByUsername(Account.AuthTypeEnum authType, String username) {
        //对用户输入登录信息以域账户形式(如 admin\\user1, admin\\user2 )进行切分，如果没有提供域信息则置为默认域
        String[] splits = username.indexOf("\\") > -1 ? StringUtils.split(username, "\\") : StringUtils.split(username, "/");
        String dataDomain, uname;
        if (splits.length == 2) {
            dataDomain = splits[0];
            uname = splits[1];
        } else {
            dataDomain = GlobalConstant.DEFAULT_VALUE;
            uname = splits[0];
        }

        //按照顺序从authUid、email、mobile查询唯一对象
        Account account = accountDao.findByDataDomainAndAuthTypeAndAuthUid(dataDomain, authType, uname);
        if (account == null) {
            account = accountDao.findByDataDomainAndAuthTypeAndEmail(dataDomain, authType, uname);
        }
        if (account == null) {
            account = accountDao.findByDataDomainAndAuthTypeAndMobile(dataDomain, authType, uname);
        }
        return account;
    }

    @Transactional(readOnly = true)
    public Account findByEmail(String email) {
        return accountDao.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Account findByMobile(String mobile) {
        return accountDao.findByMobile(mobile);
    }

    @Transactional(readOnly = true)
    public Account findByAccessToken(String accessToken) {
        return accountDao.findByAccessToken(accessToken);
    }

    public String encodeUserPasswd(Account account, String rawPassword) {
        String salt = account.getSalt();
        if (StringUtils.isBlank(salt)) {
            account.setSalt(UidUtils.buildUID());
            salt = account.getSalt();
        }
        return passwordService.entryptPassword(rawPassword, salt);
    }

    public Account save(Account entity, String rawPassword) {
        if (entity.isNew()) {
            Validation.notBlank(rawPassword, "创建账号必须提供初始密码");
            if (entity.getCredentialsExpireDate() == null) {
                //默认6个月后密码失效，到时用户登录强制要求重新设置密码
                entity.setCredentialsExpireDate(DateUtils.currentDateTime().toLocalDate().plusMonths(6));
            }
            entity.setSalt(UidUtils.buildUID());
        }

        if (StringUtils.isNotBlank(rawPassword)) {
            String encodedPassword = encodeUserPasswd(entity, rawPassword);
            if (StringUtils.isNotBlank(entity.getPassword())) {
                //为了便于开发调试，开发模式允许相同密码修改
                Validation.isTrue(AppContextHolder.isDevMode() || !entity.getPassword().equals(encodedPassword), "变更密码不能与当前密码一样");
            }
            entity.setPassword(encodedPassword);
        }

        if (entity.isNew()) {
            entity.setSignupTime(DateUtils.currentDateTime());
        }
        accountDao.save(entity);
        return entity;
    }

    public void requestResetPassword(Account account) {
        String email = account.getEmail();
        Assert.isTrue(StringUtils.isNotBlank(email), "User email required");
        String suject = dynamicConfigService.getString("cfg.account.reset.password.notify.email.title", "申请重置密码邮件");
        account.setRandomCode(UidUtils.buildUID());
        accountDao.save(account);

        String url = AppContextHolder.getWebContextUri() + ("/admin/pub/password/reset?email=" + email + "&code=" + account.getRandomCode());
        if (freemarkerService != null) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("account", account);
            params.put("resetPasswordLink", url);
            String contents = freemarkerService.processTemplateByFileName("PASSWORD_RESET_NOTIFY_EMAIL", params);
            mailService.sendHtmlMail(suject, contents, true, email);
        } else {
            mailService.sendHtmlMail(suject, url, true, email);
        }
    }
//
//    public User findByRandomCodeAndAuthUid(String randomCode, String moblie) {
//        UserExt userExt = accountDao.findByRandomCode(randomCode);
//        if (userExt != null) {
//            User user = userDao.findOne(userExt.getId());
//            if (moblie.equals(user.getMobile())) {
//                return user;
//            }
//        }
//        return null;
//    }
}
