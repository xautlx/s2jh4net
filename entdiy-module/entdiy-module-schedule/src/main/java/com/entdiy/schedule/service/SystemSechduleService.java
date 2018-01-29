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
package com.entdiy.schedule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 基于Spring Schedule XML配置的定时任务列表，适合定义在每台服务器节点执行并且无需日志记录、无需管理界面干预等功能支持的任务
 * 
 * 配置示例：
    <task:scheduled-tasks scheduler="springScheduler">
        <!-- 固定间隔触发任务，单位毫秒 -->
        <task:scheduled ref="systemSechduleService" method="statOnlineUserCount" fixed-rate="300000" />
    </task:scheduled-tasks>
 *
 */
@Component
public class SystemSechduleService {

    private final static Logger logger = LoggerFactory.getLogger(SystemSechduleService.class);

    /**
     * 统计当前在线用户数，如果超出警戒值则向管理员发送通知邮件或短信
     * @return
     */
    public Integer statOnlineUserCount() {
        logger.debug("Just mocking: statOnlineUserCount...");
        return 0;
    }
}
