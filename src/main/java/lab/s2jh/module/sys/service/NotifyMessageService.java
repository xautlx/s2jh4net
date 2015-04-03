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
import lab.s2jh.support.service.SmsService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class NotifyMessageService extends BaseService<NotifyMessage, Long> {

    @Autowired
    private NotifyMessageDao notifyMessageDao;

    @Autowired
    private NotifyMessageReadDao notifyMessageReadDao;

    @Autowired
    private SmsService smsService;

    @Override
    protected BaseDao<NotifyMessage, Long> getEntityDao() {
        return notifyMessageDao;
    }

    /**
     * 查询用户未读公告消息个数
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Long findCountToRead(User user, Integer showScope) {
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, showScope);
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
    public List<NotifyMessage> findEffectiveMessages(User user, Integer showScope) {
        List<NotifyMessage> effectiveMessages = null;
        if (user != null) {
            effectiveMessages = notifyMessageDao.findEffectiveMessages();
        } else {
            effectiveMessages = notifyMessageDao.findEffectivePubMessages();
        }

        List<NotifyMessage> scopeEffectiveMessages = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(effectiveMessages)) {
            for (NotifyMessage notifyMessage : effectiveMessages) {
                if (showScope == null || (notifyMessage.getShowScopeCode() & showScope) == showScope) {
                    scopeEffectiveMessages.add(notifyMessage);
                }
            }
        }

        return scopeEffectiveMessages;
    }

    /**
     * 查询公告消息
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public List<NotifyMessage> findEffectiveMessages(User user, Integer showScope, Boolean readState) {
        List<NotifyMessage> statedEffectiveMessages = Lists.newArrayList();
        List<NotifyMessage> scopeEffectiveMessages = findEffectiveMessages(user, showScope);
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
}
