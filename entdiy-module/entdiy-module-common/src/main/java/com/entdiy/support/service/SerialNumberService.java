/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.Date;

import static java.time.temporal.ChronoField.*;

@Service
public class SerialNumberService {

    private final Logger logger = LoggerFactory.getLogger(SerialNumberService.class);

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定义流水号工单默认前缀
     */
    private static final String SERIAL_NUMBER = "serial:generator:";

    /**
     * 对业务流水号做必要初始化，一般用于在redis服务需要重启后，当前流水号丢失，需要在业务应用启动时对相关的业务流水号进行初始化
     *
     * @param bizCode         业务前缀代码
     * @param serialNumLength 流水号位数，用于流水号截取计算
     * @param curMaxVal       当前业务单号最大值
     */
    public void init(String bizCode, int serialNumLength, String curMaxVal) {
        Assert.isTrue(StringUtils.isNotBlank(bizCode), "流水号业务类型不能为空");
        if (StringUtils.isNotBlank(curMaxVal)) {
            LocalDate now = LocalDate.now();
            //获取当前时间,返回格式如yyyyMMdd
            String date = now.format(LOCAL_DATE_FORMATTER);

            //已有当日流水号
            if (curMaxVal.indexOf(date) > -1) {
                int codeLength = curMaxVal.length();
                String num = StringUtils.substring(curMaxVal, codeLength - serialNumLength, codeLength);

                //构造redis的key
                String key = SERIAL_NUMBER + date + ":" + bizCode;
                //判断key是否存在
                Boolean exists = redisTemplate.hasKey(key);
                if (!exists) {
                    //设置当前key的value为数据库业务已有流水号值
                    long val = Long.valueOf(num);
                    logger.info("Setting redis serial num: {}={}", key, val);
                    redisTemplate.opsForValue().increment(key, val);
                    ZoneId zone = ZoneId.systemDefault();

                    //构造redis过期时间 UnixMillis
                    //设置过期时间为当天的最后一秒
                    LocalTime time = LocalTime.of(23, 59, 59);
                    LocalDateTime expireAt = LocalDateTime.of(now, time);
                    Instant instant = expireAt.atZone(zone).toInstant();
                    redisTemplate.expireAt(key, Date.from(instant));
                }
            }
        }
    }

    /**
     * 生成业务流水单号
     *
     * @param bizCode         业务前缀代码
     * @param serialNumLength 流水号位数
     * @return
     */
    public String generate(String bizCode, int serialNumLength) {
        Assert.isTrue(StringUtils.isNotBlank(bizCode), "流水号业务类型不能为空");

        LocalDate now = LocalDate.now();
        //获取当前时间,返回格式如yyyyMMdd
        String date = now.format(LOCAL_DATE_FORMATTER);

        //构造redis的key
        String key = SERIAL_NUMBER + date + ":" + bizCode;

        //判断key是否存在
        Boolean exists = redisTemplate.hasKey(key);

        Long incr = redisTemplate.opsForValue().increment(key, 1);

        //设置过期时间
        if (!exists) {
            //构造redis过期时间 UnixMillis
            //设置过期时间为当天的最后一秒
            LocalTime time = LocalTime.of(23, 59, 59);
            LocalDateTime expireAt = LocalDateTime.of(now, time);
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = expireAt.atZone(zone).toInstant();
            redisTemplate.expireAt(key, Date.from(instant));
        }

        //默认编码需要5位，位数不够前面补0
        String formattNum = String.format("%0" + serialNumLength + "d", incr);
        StringBuilder sb = new StringBuilder(20);
        //转换成业务需要的格式  bizCode + date + incr
        sb.append(bizCode).append(date).append(formattNum);

        return sb.toString();
    }
}
