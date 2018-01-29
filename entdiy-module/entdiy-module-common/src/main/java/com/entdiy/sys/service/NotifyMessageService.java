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
package com.entdiy.sys.service;

import com.entdiy.auth.entity.User;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.support.service.MessagePushService;
import com.entdiy.sys.dao.NotifyMessageDao;
import com.entdiy.sys.dao.NotifyMessageReadDao;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.NotifyMessageRead;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
     *
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Long findCountToRead(User user, String platform, String... tags) {
        if (user == null) {
            return 0L;
        }
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, platform, tags);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages)) {
            return 0L;
        }

        List<Long> scopeEffectiveMessageIds = Lists.newArrayList();
        for (NotifyMessage nm : scopeEffectiveMessages) {
            scopeEffectiveMessageIds.add(nm.getId());
        }

        List<NotifyMessageRead> notifyMessageReads = notifyMessageReadDao.findByReadUserAndNotifyMessageIn(user.getId(), scopeEffectiveMessageIds);
        return scopeEffectiveMessages.size() - (notifyMessageReads == null ? 0L : notifyMessageReads.size());
    }

    /**
     * 查询公告消息
     *
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
            List<NotifyMessage> toRemoves = Lists.newArrayList();
            for (NotifyMessage notifyMessage : scopeEffectiveMessages) {
                if (!notifyMessage.isPublic()) {
                    toRemoves.add(notifyMessage);
                }
            }
            scopeEffectiveMessages.removeAll(toRemoves);
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
            if (StringUtils.isBlank(notifyMessage.getPlatform()) || StringUtils.isBlank(platform)
                    || notifyMessage.getPlatform().indexOf(platform) != -1) {
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
                    //                    for (String tag : tags) {
                    //                        if (tag.trim().equals(user.getAlias())) {
                    //                            returnList.add(notifyMessage);
                    //                        }
                    //                    }
                    String[] aliasArray = notifyMessage.getAudienceAlias().trim().split(",");
                    for (String alias : aliasArray) {
                        if (user.getAccount().getAlias().equals(alias)) {
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
     *
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public List<NotifyMessage> findStatedEffectiveMessages(User user, String platform, Boolean readState, String... tags) {
        List<NotifyMessage> statedEffectiveMessages = Lists.newArrayList();
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, platform, tags);
        if (CollectionUtils.isEmpty(scopeEffectiveMessages) || user == null) {
            return statedEffectiveMessages;
        }

        List<Long> scopeEffectiveMessageIds = Lists.newArrayList();
        for (NotifyMessage nm : scopeEffectiveMessages) {
            scopeEffectiveMessageIds.add(nm.getId());
        }

        List<NotifyMessageRead> notifyMessageReads = notifyMessageReadDao.findByReadUserAndNotifyMessageIn(user.getId(), scopeEffectiveMessageIds);
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
            notifyMessageRead.setFirstReadTime(DateUtils.currentDateTime());
            notifyMessageRead.setLastReadTime(notifyMessageRead.getFirstReadTime());
            notifyMessageRead.setReadTotalCount(1);
            notifyMessage.setReadUserCount(notifyMessage.getReadUserCount() + 1);
        } else {
            notifyMessageRead.setLastReadTime(DateUtils.currentDateTime());
            notifyMessageRead.setReadTotalCount(notifyMessageRead.getReadTotalCount() + 1);
        }
        notifyMessageReadDao.save(notifyMessageRead);
        notifyMessageDao.save(notifyMessage);
    }

    /**
     * 定时更新公告消息生效状态
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateTobeEffectiveMessagesTimely() {
        if (logger.isDebugEnabled()) {
            logger.debug("Timely updateTobeEffectiveMessages at Thread: {}...", Thread.currentThread().getId());
        }
        List<NotifyMessage> notifyMessages = notifyMessageDao.findTobeEffectiveMessages();
        if (CollectionUtils.isNotEmpty(notifyMessages)) {
            LocalDateTime now = DateUtils.currentDateTime();
            for (NotifyMessage notifyMessage : notifyMessages) {
                Boolean oldState = notifyMessage.getEffective();
                //当前时间已过计划发布时间，则置为生效
                if (now.isAfter(notifyMessage.getPublishTime())) {
                    notifyMessage.setEffective(Boolean.TRUE);
                }
                //当前时间已过计划过期时间，则置为失效
                if (notifyMessage.getExpireTime() != null && DateUtils.currentDateTime().isAfter(notifyMessage.getExpireTime())) {
                    notifyMessage.setEffective(Boolean.FALSE);
                }
                if (notifyMessage.getEffective() != null && !notifyMessage.getEffective().equals(oldState)) {
                    logger.debug("Update notifyMessage[{}] effective state from {} to {}", notifyMessage.getDisplay(), oldState,
                            notifyMessage.getEffective());
                    notifyMessageDao.save(notifyMessage);
                    pushMessage(notifyMessage);
                }
            }
        }
    }

    /**
     * 消息推送处理
     *
     * @param entity
     */
    public void pushMessage(NotifyMessage entity) {
        if (messagePushService != null) {
            if (entity.getLastPushTime() == null && Boolean.TRUE.equals(entity.getEffective())) {
                Boolean pushResult = messagePushService.sendPush(entity);
                if (pushResult == null || pushResult) {
                    entity.setLastPushTime(DateUtils.currentDateTime());
                    notifyMessageDao.save(entity);
                }
            }
        } else {
            logger.warn("MessagePushService implement NOT found.");
        }
    }

    public List<NotifyMessage> findEffectiveMessage() {
        return notifyMessageDao.findEffectiveMessages();
    }

    @Override
    public NotifyMessage save(NotifyMessage entity) {
        super.save(entity);
        updateTobeEffectiveMessagesTimely();
        return entity;
    }
}
