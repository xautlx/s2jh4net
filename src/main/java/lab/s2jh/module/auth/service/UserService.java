package lab.s2jh.module.auth.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lab.s2jh.aud.dao.UserLogonLogDao;
import lab.s2jh.aud.entity.UserLogonLog;
import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.security.PasswordService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.Validation;
import lab.s2jh.core.util.UidUtils;
import lab.s2jh.module.auth.dao.PrivilegeDao;
import lab.s2jh.module.auth.dao.RoleDao;
import lab.s2jh.module.auth.dao.UserDao;
import lab.s2jh.module.auth.entity.Privilege;
import lab.s2jh.module.auth.entity.Role;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;
import lab.s2jh.support.service.DynamicConfigService;
import lab.s2jh.support.service.FreemarkerService;
import lab.s2jh.support.service.MailService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

@Service
@Transactional
public class UserService extends BaseService<User, Long> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private MailService mailService;

    @Autowired(required = false)
    private FreemarkerService freemarkerService;

    @Override
    protected BaseDao<User, Long> getEntityDao() {
        return userDao;
    }

    @Transactional(readOnly = true)
    public User findBySysAuthUid(String authUid) {
        return findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, authUid);
    }

    @Transactional(readOnly = true)
    public User findByAuthTypeAndAuthUid(AuthTypeEnum authType, String authUid) {
        return userDao.findByAuthTypeAndAuthUid(authType, authUid);
    }

    @Transactional(readOnly = true)
    public User findBySysAccessToken(String accessToken) {
        return findByAuthTypeAndAccessToken(AuthTypeEnum.SYS, accessToken);
    }

    @Transactional(readOnly = true)
    public User findByAuthTypeAndAccessToken(AuthTypeEnum authType, String accessToken) {
        return userDao.findByAuthTypeAndAccessToken(authType, accessToken);
    }

    public String encodeUserPasswd(User user, String rawPassword) {
        return passwordService.entryptPassword(rawPassword, user.getAuthGuid());
    }

    public User save(User entity) {
        return super.save(entity);
    }

    public User save(User entity, String rawPassword) {
        if (entity.isNew()) {
            Validation.notBlank(rawPassword, "创建账号必须提供初始密码");
            Date now = new Date();
            if (entity.getCredentialsExpireTime() == null) {
                //默认6个月后密码失效，到时用户登录强制要求重新设置密码
                entity.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
            }
            entity.setSignupTime(now);
            entity.setAuthGuid(UidUtils.UID());
        }
        if (StringUtils.isBlank(entity.getNickName())) {
            entity.setNickName(entity.getAuthUid());
        }
        if (StringUtils.isNotBlank(rawPassword)) {
            String encodedPassword = encodeUserPasswd(entity, rawPassword);
            if (StringUtils.isNotBlank(entity.getPassword())) {
                Validation.isTrue(!entity.getPassword().equals(encodedPassword), "变更密码不能与当前密码一样");
            }
            entity.setPassword(encodedPassword);
        }
        return userDao.save(entity);
    }

    public User saveCascadeR2Roles(User entity, String rawPassword) {
        updateRelatedR2s(entity, entity.getSelectedRoleIds(), "userR2Roles", "role");
        return save(entity, rawPassword);
    }

    @Transactional(readOnly = true)
    public List<Role> findRoles(User user) {
        return roleDao.findRolesForUser(user);
    }

    @Transactional(readOnly = true)
    public List<Privilege> findPrivileges(Set<String> roleCodes) {
        return privilegeDao.findPrivileges(roleCodes);
    }

    public void requestResetPassword(String webContextUrl, User user) {
        String email = user.getEmail();
        Assert.isTrue(StringUtils.isNotBlank(email), "User email required");
        String suject = dynamicConfigService.getString("cfg.user.reset.pwd.notify.email.title", "申请重置密码邮件");
        user.setRandomCode(UUID.randomUUID().toString());
        userDao.save(user);

        webContextUrl += ("/admin/password/reset?uid=" + user.getAuthUid() + "&email=" + email + "&code=" + user.getRandomCode());

        if (freemarkerService != null) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("user", user);
            params.put("resetPasswordLink", webContextUrl.toString());
            String contents = freemarkerService.processTemplateByFileName("PASSWORD_RESET_NOTIFY_EMAIL", params);
            mailService.sendHtmlMail(suject, contents, true, email);
        } else {
            mailService.sendHtmlMail(suject, webContextUrl.toString(), true, email);
        }
    }

    @Async
    public void userLogonLog(User user, UserLogonLog userLogonLog) {

        String httpSessionId = userLogonLog.getHttpSessionId();
        if (StringUtils.isNotBlank(httpSessionId)) {
            if (userLogonLogDao.findByHttpSessionId(httpSessionId) != null) {
                return;
            }
        }

        //登录记录
        user.setLogonTimes(user.getLogonTimes() + 1);
        user.setLastLogonIP(userLogonLog.getRemoteAddr());
        user.setLastLogonHost(userLogonLog.getRemoteHost());
        user.setLastLogonTime(new Date());

        //重置失败次数计数
        user.setLastLogonFailureTime(null);
        user.setLogonFailureTimes(0);
        userDao.save(user);

        userLogonLog.setLogonTimes(user.getLogonTimes());
        userLogonLogDao.save(userLogonLog);
    }

    public User findByAuthUid(String authUid) {
        return userDao.findByAuthUid(authUid);
    }

    public User findByRandomCodeAndAuthUid(String randomCode, String moblie) {
        return userDao.findByRandomCodeAndAuthUid(randomCode, moblie);
    }
}
