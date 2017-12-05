/**
 * Copyright (c) 2012
 */
package com.entdiy.security;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.context.SpringContextHolder;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以ThreadLocal方式实现Web端登录信息传递到业务层的存取
 */
public class AuthContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(AuthContextHolder.class);

    public static final String DEFAULT_UNKNOWN_PIN = "N/A";

    /**
     * 获取SYS类型账户登录账号
     */
    public static String getAuthSysUserUid() {
        AuthUserDetails authUserDetails = getAuthUserDetails();
        if (authUserDetails == null || !AuthTypeEnum.SYS.equals(authUserDetails.getAuthType())) {
            return null;
        }
        return authUserDetails.getAuthUid();
    }

    /**
     * 获取登录用户的友好显示字符串
     */
    public static String getAuthUserDisplay() {
        AuthUserDetails authUserDetails = getAuthUserDetails();
        if (authUserDetails != null) {
            return authUserDetails.getAuthDisplay();
        }
        return DEFAULT_UNKNOWN_PIN;
    }

    /**
     * 基于Spring Security获取用户认证信息
     */
    public static AuthUserDetails getAuthUserDetails() {
        Subject subject = null;
        try {
            subject = SecurityUtils.getSubject();
        } catch (Exception e) {
            logger.trace(e.getMessage());
        }
        if (subject == null) {
            return null;
        }
        Object principal = null;
        try {
            principal = subject.getPrincipal();
        } catch (InvalidSessionException e) {
            //如果是没有有效的Shiro Session则直接返回null，避免在后台处理时相关审计处理逻辑代码异常
            logger.trace(e.getMessage());
        }
        if (principal == null) {
            return null;
        }
        return (AuthUserDetails) principal;
    }

    /**
     * 获取当前登录用户Entity实体对象
     */
    public static User findAuthUser() {
        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails == null) {
            return null;
        }
        UserService userService = SpringContextHolder.getBean(UserService.class);
        return userService.findByAuthTypeAndAuthUid(authUserDetails.getAuthType(), authUserDetails.getAuthUid());
    }
}
