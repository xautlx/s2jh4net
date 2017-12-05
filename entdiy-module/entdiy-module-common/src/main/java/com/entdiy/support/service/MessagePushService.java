package com.entdiy.support.service;

import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.UserMessage;

/**
 * 消息APP推送服务接口
 */
public interface MessagePushService {

    /**
     * 公告消息推送接口
     * @return 推送结果：null=无需推送，true=推送成功；false=推送失败
     */
    Boolean sendPush(NotifyMessage notifyMessage);

    /**
     * 个人消息推送接口
     * @return 推送结果：null=无需推送，true=推送成功；false=推送失败
     */
    Boolean sendPush(UserMessage userMessage);
}
