package lab.s2jh.module.sys.job;

import java.util.Date;
import java.util.List;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.module.schedule.BaseQuartzJobBean;
import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.UserMessage;
import lab.s2jh.module.sys.service.NotifyMessageService;
import lab.s2jh.module.sys.service.UserMessageService;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于Quartz集群模式配置每分钟运行：消息定时更新有效性
 */
@MetaData("消息定时更新有效性")
public class MessageUpdateJob extends BaseQuartzJobBean {

    private final static Logger logger = LoggerFactory.getLogger(MessageUpdateJob.class);

    @Override
    protected String executeInternalBiz(JobExecutionContext context) {
        //对于NotifyMessage和UserMessage对象，分别基于publishTime和expireTime更新effective属性

        NotifyMessageService notifyMessageService = getSpringBean(NotifyMessageService.class);
        UserMessageService userMessageService = getSpringBean(UserMessageService.class);
        Date now = DateUtils.currentDate();
        //检查有效消息

        StringBuffer sb = new StringBuffer();
        Integer num = notifyMessageService.updateNotifyMessageEffective(now);
        logger.debug("更新有效公告信息数量: {}", num);
        sb.append("更新有效公告信息数量: " + num + ";");

        num = userMessageService.updateUserMessageEffective(now);
        logger.debug("更新有效个人信息数量: {}", num);
        sb.append("更新有效个人信息数量: " + num + ";");

        num = notifyMessageService.updateNotifyMessageNoneffective(now);
        logger.debug("更新无效公告信息数量: {}", num);
        sb.append("更新无效公告信息数量: " + num + ";");

        num = userMessageService.updateUserMessageNoneffective(now);
        logger.debug("更新无效个人信息数量: {}", num);
        sb.append("更新无效个人信息数量: " + num + ";");

        //找到lastPushTime为空，并且已经有效的消息调用推送接口实现初始化消息推送
        int notifyMessageNum = 0;
        List<NotifyMessage> notifyMessages = notifyMessageService.findEffectiveMessage();
        if (CollectionUtils.isNotEmpty(notifyMessages)) {
            for (NotifyMessage message : notifyMessages) {
                if (message.getLastPushTime() == null) {
                    notifyMessageService.pushMessage(message);
                    notifyMessageNum++;
                }
            }
        }
        logger.debug("推送公告消息数量: {}", notifyMessageNum);
        sb.append("推送公告消息数量: " + notifyMessageNum + ";");

        int userMessageNum = 0;
        List<UserMessage> userMessages = userMessageService.findEffectiveMessages();
        if (CollectionUtils.isNotEmpty(userMessages)) {
            for (UserMessage message : userMessages) {
                userMessageService.pushMessage(message);
                userMessageNum++;
            }
        }

        logger.debug("推送个人消息数量: {}", userMessageNum);
        sb.append("推送个人消息数量: " + userMessageNum + ";");

        return "更新消息数量：" + sb.toString();
    }

}
