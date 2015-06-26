package lab.s2jh.module.sys.service;

import java.util.Date;
import java.util.List;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.dao.UserMessageDao;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.support.service.MailService;
import lab.s2jh.support.service.MessagePushService;
import lab.s2jh.support.service.SmsService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMessageService extends BaseService<UserMessage, Long> {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageService.class);

    @Autowired
    private UserMessageDao userMessageDao;

    @Autowired(required = false)
    private MailService mailService;

    @Autowired(required = false)
    private SmsService smsService;

    @Autowired(required = false)
    private MessagePushService messagePushService;

    @Override
    protected BaseDao<UserMessage, Long> getEntityDao() {
        return userMessageDao;
    }

    /**
     * 查询用户未读消息个数
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Long findCountToRead(User user) {
        GroupPropertyFilter filter = GroupPropertyFilter.buildDefaultAndGroupFilter();
        filter.append(new PropertyFilter(MatchType.EQ, "effective", Boolean.TRUE));
        filter.append(new PropertyFilter(MatchType.EQ, "targetUser", user));
        filter.append(new PropertyFilter(MatchType.NU, "firstReadTime", Boolean.TRUE));
        return count(filter);
    }

    public void processUserRead(UserMessage entity, User user) {
        if (entity.getFirstReadTime() == null) {
            entity.setFirstReadTime(DateUtils.currentDate());
            entity.setLastReadTime(entity.getFirstReadTime());
            entity.setReadTotalCount(1);
        } else {
            entity.setLastReadTime(DateUtils.currentDate());
            entity.setReadTotalCount(entity.getReadTotalCount() + 1);
        }
        userMessageDao.save(entity);
    }

    public Integer updateUserMessageEffective(Date now) {
        return userMessageDao.updateUserMessageEffective(now);
    }

    public Integer updateUserMessageNoneffective(Date now) {
        return userMessageDao.updateUserMessageNoneffective(now);
    }

    /**
     * 消息推送处理
     * @param entity
     */
    public void pushMessage(UserMessage entity) {
        //定向用户消息处理
        User targetUser = entity.getTargetUser();

        //邮件推送处理
        if (entity.getEmailPush()) {
            String email = targetUser.getEmail();
            if (StringUtils.isNotBlank(email)) {
                mailService.sendHtmlMail(entity.getTitle(), entity.getMessage(), true, email);
            }
        }

        //短信推送处理
        if (entity.getSmsPush()) {
            if (smsService != null) {
                String mobileNum = targetUser.getMobile();
                if (StringUtils.isNotBlank(mobileNum)) {
                    smsService.sendSMS(entity.getNotification(), mobileNum);
                }
            } else {
                logger.warn("SmsService implement NOT found.");
            }

        }

        //APP推送
        if (entity.getAppPush()) {
            if (messagePushService != null) {
                messagePushService.sendPush(entity);
            } else {
                logger.warn("MessagePushService implement NOT found.");
            }
        }

        entity.setLastPushTime(DateUtils.currentDate());
        userMessageDao.save(entity);
    }

    public List<UserMessage> findEffectiveMessages() {
        return userMessageDao.findEffectiveMessages();
    }
}
