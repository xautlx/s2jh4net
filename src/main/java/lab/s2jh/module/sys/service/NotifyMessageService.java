package lab.s2jh.module.sys.service;

import java.util.Date;
import java.util.List;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.dao.NotifyMessageDao;
import lab.s2jh.module.sys.dao.NotifyMessageReadDao;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;
import lab.s2jh.support.service.MessagePushService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class NotifyMessageService extends BaseService<NotifyMessage, Long> {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageService.class);

    @Autowired
    private NotifyMessageDao notifyMessageDao;

    @Autowired
    private NotifyMessageReadDao notifyMessageReadDao;

    @Autowired(required = false)
    private MessagePushService messagePushService;

    @Override
    protected BaseDao<NotifyMessage, Long> getEntityDao() {
        return notifyMessageDao;
    }

    /**
     * 查询用户未读公告消息个数
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Long findCountToRead(User user, String platform, String... tags) {
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, platform, tags);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages)) {
            return 0L;
        }

        List<NotifyMessageRead> notifyMessageReads = notifyMessageReadDao.findByReadUserAndNotifyMessageIn(user, scopeEffectiveMessages);
        return scopeEffectiveMessages.size() - (notifyMessageReads == null ? 0L : notifyMessageReads.size());
    }

    /**
     * 查询公告消息
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public List<NotifyMessage> findEffectiveMessages(User user, String platform, String... tags) {
        List<NotifyMessage> effectiveMessages = notifyMessageDao.findEffectiveMessages();

        List<NotifyMessage> scopeEffectiveMessages = Lists.newArrayList();

        if (CollectionUtils.isEmpty(effectiveMessages)) {
            return scopeEffectiveMessages;
        }

        //可参考 http://docs.jpush.io/server/rest_api_v3_push/#audience
        //每种类型的值都是数组（Array），数组里多个值之间隐含的关系是是 OR，即取并集。但 tag_and 不同，其数组里多个值之间是 AND 关系，即取交集。
        //4 种类型至少需要有其一。如果值数组长度为 0，表示该类型不存在。
        //这几种类型可以并存。并存时多项的隐含关系是 AND，即取交集。

        //基于audienceXXX判断当前用户公告列表

        scopeEffectiveMessages = filterByPlatform(effectiveMessages, platform);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages)) {
            return scopeEffectiveMessages;
        }

        if (tags == null || tags.length == 0) {
            for (NotifyMessage notifyMessage : scopeEffectiveMessages) {
                if (!notifyMessage.isPublic()) {
                    scopeEffectiveMessages.remove(notifyMessage);
                }
            }
            return scopeEffectiveMessages;
        }

        scopeEffectiveMessages = filterByAudienceTags(scopeEffectiveMessages, tags);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages)) {
            return scopeEffectiveMessages;
        }

        scopeEffectiveMessages = filterByAudienceAndTags(scopeEffectiveMessages, tags);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages)) {
            return scopeEffectiveMessages;
        }

        scopeEffectiveMessages = filterByAudienceAlias(scopeEffectiveMessages, user);

        return scopeEffectiveMessages;
    }

    private List<NotifyMessage> filterByPlatform(List<NotifyMessage> notifyMessages, String platform) {
        List<NotifyMessage> returnList = Lists.newArrayList();
        for (NotifyMessage notifyMessage : notifyMessages) {
            if (StringUtils.isBlank(notifyMessage.getPlatform()) || platform.equals(notifyMessage.getPlatform())) {
                returnList.add(notifyMessage);
            }
        }

        return returnList;
    }

    private List<NotifyMessage> filterByAudienceTags(List<NotifyMessage> notifyMessages, String... tags) {
        List<NotifyMessage> returnList = Lists.newArrayList();
        for (NotifyMessage notifyMessage : notifyMessages) {
            if (StringUtils.isBlank(notifyMessage.getAudienceTags()) || notifyMessage.isPublic()) {
                returnList.add(notifyMessage);
            } else {

                String[] setTags = notifyMessage.getAudienceTags().trim().split(",");

                for (String tag : tags) {
                    boolean scope = false;
                    for (String setTag : setTags) {
                        if (setTag.trim().equals(tag.trim())) {
                            scope = true;
                            break;
                        }
                    }
                    if (scope) {
                        returnList.add(notifyMessage);
                        break;
                    }
                }
            }
        }

        return returnList;
    }

    private List<NotifyMessage> filterByAudienceAndTags(List<NotifyMessage> notifyMessages, String... tags) {
        List<NotifyMessage> returnList = Lists.newArrayList();
        for (NotifyMessage notifyMessage : notifyMessages) {
            if (StringUtils.isBlank(notifyMessage.getAudienceAndTags()) || notifyMessage.isPublic()) {
                returnList.add(notifyMessage);
            } else {
                String[] setTags = notifyMessage.getAudienceTags().trim().split(",");
                boolean scope = true;
                for (String setTag : setTags) {
                    for (String tag : tags) {
                        if (setTag.trim().equals(tag.trim())) {
                            scope = true;
                            break;
                        } else {
                            scope = false;
                        }
                    }
                    if (!scope) {
                        break;
                    }
                }
                if (scope) {
                    returnList.add(notifyMessage);
                }
            }
        }

        return returnList;
    }

    private List<NotifyMessage> filterByAudienceAlias(List<NotifyMessage> notifyMessages, User user, String... tags) {
        List<NotifyMessage> returnList = Lists.newArrayList();
        for (NotifyMessage notifyMessage : notifyMessages) {
            if (StringUtils.isBlank(notifyMessage.getAudienceAlias()) || notifyMessage.isPublic()) {
                returnList.add(notifyMessage);
            } else {
                if (user != null) {
                    for (String tag : tags) {
                        if (tag.trim().equals(user.getAlias())) {
                            returnList.add(notifyMessage);
                        }
                    }
                }
            }
        }

        return returnList;
    }

    /**
     * 查询公告消息
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public List<NotifyMessage> findStatedEffectiveMessages(User user, String platform, Boolean readState, String... tags) {
        List<NotifyMessage> statedEffectiveMessages = Lists.newArrayList();
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, platform, tags);
        List<NotifyMessageRead> notifyMessageReads = notifyMessageReadDao.findByReadUserAndNotifyMessageIn(user, scopeEffectiveMessages);
        for (NotifyMessage notifyMessage : scopeEffectiveMessages) {
            boolean readed = false;
            for (NotifyMessageRead notifyMessageRead : notifyMessageReads) {
                if (notifyMessageRead.getNotifyMessage().getId().equals(notifyMessage.getId())) {
                    readed = true;
                    break;
                }
            }
            notifyMessage.setReaded(readed);

            if (readState == null || readState.equals(notifyMessage.getReaded())) {
                statedEffectiveMessages.add(notifyMessage);
            }
        }

        return statedEffectiveMessages;
    }

    public void processUserRead(NotifyMessage notifyMessage, User user) {
        NotifyMessageRead notifyMessageRead = notifyMessageReadDao.findByNotifyMessageAndReadUser(notifyMessage, user);
        if (notifyMessageRead == null) {
            notifyMessageRead = new NotifyMessageRead();
            notifyMessageRead.setNotifyMessage(notifyMessage);
            notifyMessageRead.setReadUser(user);
            notifyMessageRead.setFirstReadTime(new Date());
            notifyMessageRead.setLastReadTime(notifyMessageRead.getFirstReadTime());
            notifyMessageRead.setReadTotalCount(1);
            notifyMessage.setReadUserCount(notifyMessage.getReadUserCount() + 1);
        } else {
            notifyMessageRead.setLastReadTime(new Date());
            notifyMessageRead.setReadTotalCount(notifyMessageRead.getReadTotalCount() + 1);
        }
        notifyMessageReadDao.save(notifyMessageRead);
        notifyMessageDao.save(notifyMessage);
    }

    public Integer updateNotifyMessageEffective(Date now) {
        return notifyMessageDao.updateNotifyMessageEffective(now);
    }

    public Integer updateNotifyMessageNoneffective(Date now) {
        return notifyMessageDao.updateNotifyMessageNoneffective(now);
    }

    /**
     * 消息推送处理
     * @param entity
     */
    public void pushMessage(NotifyMessage entity) {
        if (messagePushService != null) {
            if (messagePushService.sendPush(entity)) {
                entity.setLastPushTime(new Date());
                notifyMessageDao.save(entity);
            }
        } else {
            logger.warn("MessagePushService implement NOT found.");
        }
    }

    public List<NotifyMessage> findEffectiveMessage() {
        return notifyMessageDao.findEffectiveMessages();
    }
}
