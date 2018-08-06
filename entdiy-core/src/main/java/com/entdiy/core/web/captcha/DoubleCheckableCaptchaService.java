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
package com.entdiy.core.web.captcha;

import com.entdiy.core.exception.ValidationException;
import com.entdiy.core.service.Validation;
import com.google.common.collect.Maps;
import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * 默认的validateResponseForID无论校验成功与否都会移除ID当前存储验证码导致必须重新刷新验证码。
 * 新增一个"预校验"接口以满足一些AJAX或APP等友好的提前校验需求。
 */
public class DoubleCheckableCaptchaService extends GenericManageableCaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(DoubleCheckableCaptchaService.class);

    private static Map<String, Integer> failureCounter = Maps.newHashMap();
    private static final int FAILURE_RETRY_LIMIT = 3;

    public DoubleCheckableCaptchaService(CaptchaEngine captchaEngine,
                                         int minGuarantedStorageDelayInSeconds,
                                         int maxCaptchaStoreSize,
                                         int captchaStoreLoadBeforeGarbageCollection) {
        super(new FastHashMapCaptchaStore(), captchaEngine, minGuarantedStorageDelayInSeconds,
                maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    public DoubleCheckableCaptchaService(CaptchaStore captchaStore,
                                         CaptchaEngine captchaEngine,
                                         int minGuarantedStorageDelayInSeconds,
                                         int maxCaptchaStoreSize,
                                         int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds,
                maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    /**
     * 每次调用都重新生成
     *
     * @param ID
     * @param locale
     * @return
     * @throws CaptchaServiceException
     */
    @Override
    public Object getChallengeForID(String ID, Locale locale) throws CaptchaServiceException {
        Captcha captcha;
        if (!this.store.hasCaptcha(ID)) {
            this.store.removeCaptcha(ID);
        }
        captcha = this.generateAndStoreCaptcha(locale, ID);
        if (captcha == null) {
            captcha = this.generateAndStoreCaptcha(locale, ID);
        } else if (captcha.hasGetChalengeBeenCalled()) {
            captcha = this.generateAndStoreCaptcha(locale, ID);
        }
        Object challenge = this.getChallengeClone(captcha);
        captcha.disposeChallenge();
        //重置计数器
        failureCounter.put(ID, 0);
        return challenge;
    }

    /**
     * 默认实现策略是每次校验无论失败与否都立即删除当前校验码需要重新生成，虽然安全性最高但是对于用户体验来说可能不太友好
     * 为此调整实现为只有验证成功后才移除验证码，但是这样存在一个风险就是恶意用户可能反复尝试破解验证码
     * 为了能在安全性和友好性找到一个平衡实现，添加一个校验失败的计数器
     * 为了简化处理计数器没有采用集群存储而是直接采用HashMap作为计数器容器，这样潜在的问题可能就是失败校验次数会放大为 failureRetryLimit*集群节点数目
     * 考虑到一般中小型项目部署集群节点也就几台，可以容忍这样的次数放大。如果是需要更加严格的失败次数控制可考虑改为集群存储计数器形式。
     */
    private void validateFailureChecking(String ID) {
        Integer failureTimes = failureCounter.get(ID);
        if (failureTimes != null && failureTimes > FAILURE_RETRY_LIMIT) {
            //如果超过失败限制次数，移除Captcha
            this.store.removeCaptcha(ID);

            throw new ValidationException("验证码已失效，请重新输入");
        }
        failureCounter.put(ID, failureTimes == null ? 1 : failureTimes + 1);
    }

    /**
     * 重写validateResponseForID方法,默认每次经过该操作都会删除对应的Captcha
     *
     * @param ID       SessionID
     * @param response 提交的验证码参数值
     */
    @Override
    public Boolean validateResponseForID(String ID, Object response) {
        logger.debug("validateResponseForID ID={}, captcha={}", ID, response);
        Validation.isTrue(store.hasCaptcha(ID), "验证码已失效，请重新输入");

        Boolean valid = store.getCaptcha(ID).validateResponse(response);
        if (valid) {
            //如果验证成功，移除Captcha
            this.store.removeCaptcha(ID);
        } else {
            validateFailureChecking(ID);
        }
        return valid;
    }

    /**
     * 预校验接口，可用于AJAX或APP等remote表单校验实时提示用户验证码正确与否。
     * 注意：预校验主要是为了客户端的提示友好，在最终的表单服务端处理还应该调用标准的validateResponseForID进行二次校验，防止用户绕过表单校验直接提交非法表单请求
     *
     * @param ID
     * @param response
     * @return
     * @throws CaptchaServiceException
     */
    public Boolean touchValidateResponseForID(String ID, Object response) {
        logger.debug("touchValidateResponseForID ID={}, captcha={}", ID, response);
        Validation.isTrue(store.hasCaptcha(ID), "验证码已失效，请重新输入");
        boolean valid = store.getCaptcha(ID).validateResponse(response);
        if (!valid) {
            validateFailureChecking(ID);
        }
        return valid;
    }
}
