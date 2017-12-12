/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.web.captcha;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的validateResponseForID无论校验成功与否都会移除ID当前存储验证码导致必须重新刷新验证码。
 * 新增一个"预校验"接口以满足一些AJAX或APP等友好的提前校验需求。
 */
public class DoubleCheckableCaptchaService extends GenericManageableCaptchaService {
    private static final Logger logger = LoggerFactory.getLogger(DoubleCheckableCaptchaService.class);

    public DoubleCheckableCaptchaService(CaptchaEngine captchaEngine,
                                         int minGuarantedStorageDelayInSeconds,
                                         int maxCaptchaStoreSize,
                                         int captchaStoreLoadBeforeGarbageCollection) {
        super(new FastHashMapCaptchaStore(), captchaEngine, minGuarantedStorageDelayInSeconds,
                maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
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
    public Boolean touchValidateResponseForID(String ID, Object response) throws CaptchaServiceException {
        logger.debug("touchValidateResponseForID ID={}, captcha={}", ID, response);
        if (!store.hasCaptcha(ID)) {
            throw new CaptchaServiceException("Invalid ID, could not validate unexisting or already validated captcha");
        } else {
            return store.getCaptcha(ID).validateResponse(response);
        }
    }
}
