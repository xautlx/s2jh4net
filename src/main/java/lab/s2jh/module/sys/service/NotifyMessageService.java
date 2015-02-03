package lab.s2jh.module.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.dao.mybatis.MyBatisDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.dao.NotifyMessageDao;
import lab.s2jh.module.sys.dao.NotifyMessageReadDao;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.NotifyMessageRead;
import lab.s2jh.support.service.SmsService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class NotifyMessageService extends BaseService<NotifyMessage, Long> {

    @Autowired
    private MyBatisDao myBatisDao;

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
     * 查询用户管理端未读消息个数
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Integer findMgmtCountToRead(User user) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("user", user);
        parameters.put("mgmtShow", Boolean.TRUE);
        parameters.put("now", new Date());
        return myBatisDao.findOne(NotifyMessage.class.getName(), "findCountToRead", parameters);
    }

    /**
     * 查询用户前端未读消息个数
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Integer findSiteCountToRead(User user) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("user", user);
        parameters.put("siteShow", Boolean.TRUE);
        parameters.put("now", new Date());
        return myBatisDao.findOne(NotifyMessage.class.getName(), "findCountToRead", parameters);
    }

    /**
     * 查询用户管理端消息列表
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Page<NotifyMessage> findMgmtPageToRead(User user, Map<String, Object> parameters, Pageable pageable) {
        if (parameters == null) {
            parameters = Maps.newHashMap();
        }
        parameters.put("user", user);
        parameters.put("mgmtShow", Boolean.TRUE);
        parameters.put("now", new Date());
        return myBatisDao.findPage(NotifyMessage.class.getName(), "findPageToUser", parameters, pageable);
    }

    /**
     * 查询用户前端消息列表
     * @param user 当前登录用户
     */
    @Transactional(readOnly = true)
    public Page<NotifyMessage> findSitePageToRead(User user, Map<String, Object> parameters, Pageable pageable) {
        if (parameters == null) {
            parameters = Maps.newHashMap();
        }
        parameters.put("user", user);
        parameters.put("siteShow", Boolean.TRUE);
        parameters.put("now", new Date());
        return myBatisDao.findPage(NotifyMessage.class.getName(), "findPageToUser", parameters, pageable);
    }

    public void processSiteUserReadAll(User user) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("user", user);
        parameters.put("siteShow", Boolean.TRUE);
        parameters.put("toRead", Boolean.TRUE);
        parameters.put("now", new Date());
        List<NotifyMessage> notifyMessages = myBatisDao.findList(NotifyMessage.class.getName(), "findPageToUser", parameters);
        if (CollectionUtils.isNotEmpty(notifyMessages)) {
            for (NotifyMessage notifyMessage : notifyMessages) {
                //通过MyBatis获取数据重新从Hibernate查询返回
                notifyMessage = notifyMessageDao.findOne(notifyMessage.getId());
                processUserRead(notifyMessage, user);
            }
        }
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

    @Override
    public NotifyMessage save(NotifyMessage entity) {
        boolean newEntity = entity.isNew();
        super.save(entity);
        if (newEntity) {
            //定向用户消息处理
            User targetUser = entity.getTargetUser();
            if (targetUser != null) {
                //邮件推送处理
                if (entity.getEmailPush()) {
                    //TODO Email send
                }
                //短信推送处理
                if (entity.getSmsPush()) {
                    String mobileNum = targetUser.getMobile();
                    if (StringUtils.isNotBlank(mobileNum)) {
                        smsService.sendSMS(entity.getHtmlContent(), mobileNum);
                    }
                }
            }
        }
        return entity;
    }
}
