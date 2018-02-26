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
package com.entdiy.support.service;

import com.entdiy.sys.entity.AccountMessage;
import com.entdiy.sys.entity.NotifyMessage;

/**
 * 消息APP推送服务接口
 */
public interface MessagePushService {

    /**
     * 公告消息推送接口
     *
     * @return 推送结果：null=无需推送，true=推送成功；false=推送失败
     */
    Boolean sendPush(NotifyMessage notifyMessage);

    /**
     * 个人消息推送接口
     *
     * @return 推送结果：null=无需推送，true=推送成功；false=推送失败
     */
    Boolean sendPush(AccountMessage accountMessage);
}
