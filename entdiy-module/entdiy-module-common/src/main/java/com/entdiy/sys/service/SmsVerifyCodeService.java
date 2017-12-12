/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.sys.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.service.GlobalConfigService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.sys.dao.SmsVerifyCodeDao;
import com.entdiy.sys.entity.SmsVerifyCode;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SmsVerifyCodeService extends BaseService<SmsVerifyCode, Long> {

    private static final Logger logger = LoggerFactory.getLogger(SmsVerifyCodeService.class);

    private final static int VERIFY_CODE_LIVE_MINUTES = 60;

    @Autowired
    private SmsVerifyCodeDao smsVerifyCodeDao;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Override
    protected BaseDao<SmsVerifyCode, Long> getEntityDao() {
        return smsVerifyCodeDao;
    }

    /**
     * 基于手机号码生成6位随机验证码
     * @param mobileNum 手机号码
     * @return 6位随机数
     */
    public String generateSmsCode(HttpServletRequest request, String mobileNum, boolean mustExist) {
        if (mustExist && dynamicConfigService.getBoolean(GlobalConstant.cfg_public_send_sms_disabled, false)) {
            throw new ServiceException("非注册手机号短信发送功能已暂停");
        }

        SmsVerifyCode smsVerifyCode = smsVerifyCodeDao.findByMobileNum(mobileNum);
        boolean updateCode = false;
        if (smsVerifyCode == null) {
            //要求手机号之前必须已经成功验证过
            if (mustExist && !GlobalConfigService.isDevMode()) {
                throw new ServiceException("手机号无效");
            }

            updateCode = true;
            smsVerifyCode = new SmsVerifyCode();
            smsVerifyCode.setMobileNum(mobileNum);
        } else {
            //已过期
            if (DateUtils.currentDate().after(smsVerifyCode.getExpireTime())) {
                updateCode = true;
            }
        }

        //如果需要更新则刷新验证码，否则直接返回之前未过期的验证码
        if (updateCode) {
            String code = RandomStringUtils.randomNumeric(6);
            smsVerifyCode.setCode(code);
            smsVerifyCode.setGenerateTime(DateUtils.currentDate());
            //5分钟有效期
            smsVerifyCode.setExpireTime(new DateTime(smsVerifyCode.getGenerateTime()).plusMinutes(VERIFY_CODE_LIVE_MINUTES).toDate());
            smsVerifyCodeDao.save(smsVerifyCode);
        }

        return smsVerifyCode.getCode();
    }

    /**
     * 验证手机验证码有效性
     * @param mobileNum 手机号码
     * @param code 验证码
     * @return 布尔类型是否有效
     */
    public boolean verifySmsCode(HttpServletRequest request, String mobileNum, String code) {
        //如果是开发模式，则123456作为默认验证码始终通过，方便开发测试
        if (GlobalConfigService.isDevMode()) {
            if ("123456".equals(code)) {
                return true;
            }
        }
        SmsVerifyCode smsVerifyCode = smsVerifyCodeDao.findByMobileNum(mobileNum);
        //未找到记录验证码
        if (smsVerifyCode == null) {
            return false;
        }
        Date now = DateUtils.currentDate();
        //验证码已过期
        if (smsVerifyCode.getExpireTime().before(now)) {
            return false;
        }
        boolean pass = smsVerifyCode.getCode().equals(code);
        if (pass) {
            if (smsVerifyCode.getFirstVerifiedTime() == null) {
                smsVerifyCode.setFirstVerifiedTime(now);
            }
            smsVerifyCode.setTotalVerifiedCount(smsVerifyCode.getTotalVerifiedCount() + 1);
            smsVerifyCode.setLastVerifiedTime(now);
            smsVerifyCodeDao.save(smsVerifyCode);
        }
        return pass;
    }

    /**
     * 定时把超时的验证码移除  
     */
    //@Scheduled(fixedRate = 60 * 60 * 1000)
    public void removeExpiredDataTimely() {
        logger.debug("Timely trigger removed expired verify code cache data at Thread: {}...", Thread.currentThread().getId());
        if (smsVerifyCodeDao.countTodoItems() > 0) {
            int effectiveCount = smsVerifyCodeDao.batchDeleteExpireItems(DateUtils.currentDate());
            logger.debug("Removed expired verify code cache data number: {}", effectiveCount);
        }
    }
}
