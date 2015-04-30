package lab.s2jh.support.service;

import lab.s2jh.module.sys.entity.NotifyMessage;
import lab.s2jh.module.sys.entity.UserMessage;

/**
 * 消息APP推送服务接口
 */
public interface MessagePushService {

    /**
     * 公告消息推送接口
     */
    boolean sendPush(NotifyMessage notifyMessage);

    /**
     * 个人消息推送接口
     */
    boolean sendPush(UserMessage userMessage);
}
