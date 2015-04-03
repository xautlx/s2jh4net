package lab.s2jh.module.sys.service;

import java.util.Date;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.dao.UserMessageDao;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.support.service.SmsService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMessageService extends BaseService<UserMessage, Long> {

    @Autowired
    private UserMessageDao userMessageDao;

    @Autowired
    private SmsService smsService;

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
            entity.setFirstReadTime(new Date());
            entity.setLastReadTime(entity.getFirstReadTime());
            entity.setReadTotalCount(1);
        } else {
            entity.setLastReadTime(new Date());
            entity.setReadTotalCount(entity.getReadTotalCount() + 1);
        }
        userMessageDao.save(entity);
    }

    @Override
    public UserMessage save(UserMessage entity) {
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
