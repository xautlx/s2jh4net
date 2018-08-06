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
package com.entdiy.core.web.captcha;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;

/**
 * 基于Redis管理的CaptchaStore
 */
public class RedisCaptchaStore implements CaptchaStore {

    private static final Logger CAPTCHA_LOG = LoggerFactory.getLogger(RedisCaptchaStore.class);

    private static final String CAPTCHA_SESSION_KEY = "captcha_store:";

    private RedisTemplate<String, Serializable> redisTemplate;
    private HashOperations<String, String, Serializable> hashOperations;

    @Override
    public boolean hasCaptcha(String sid) {
        return this.hashOperations.hasKey(CAPTCHA_SESSION_KEY, sid);
    }

    @Override
    public void storeCaptcha(String sid, Captcha captcha) throws CaptchaServiceException {
        CAPTCHA_LOG.debug("Store captcha, Sid: {}.", sid);
        this.hashOperations.put(CAPTCHA_SESSION_KEY, sid, captcha);
    }

    @Override
    public void storeCaptcha(String sid, Captcha captcha, Locale locale) throws CaptchaServiceException {
        CAPTCHA_LOG.debug("Store captcha, Sid: {}.", sid);
        this.hashOperations.put(CAPTCHA_SESSION_KEY, sid, new CaptchaAndLocale(captcha, locale));
    }

    @Override
    public boolean removeCaptcha(String sid) {
        if (this.hasCaptcha(sid)) {
            CAPTCHA_LOG.debug("Remove captcha, Sid: {}.", sid);
            this.hashOperations.delete(CAPTCHA_SESSION_KEY, sid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Captcha getCaptcha(String sid) throws CaptchaServiceException {
        Object val = this.hashOperations.get(CAPTCHA_SESSION_KEY, sid);
        if (val != null) {
            if (val instanceof Captcha) {
                return (Captcha) val;
            }
            if (val instanceof CaptchaAndLocale) {
                return ((CaptchaAndLocale) val).getCaptcha();
            }
        }
        return null;
    }

    @Override
    public Locale getLocale(String sid) throws CaptchaServiceException {
        Object captchaAndLocale = this.getCaptcha(sid);
        if (captchaAndLocale != null && captchaAndLocale instanceof CaptchaAndLocale) {
            return ((CaptchaAndLocale) captchaAndLocale).getLocale();
        }
        return null;
    }

    @Override
    public int getSize() {
        CAPTCHA_LOG.debug("Get captcha size.");
        return Math.toIntExact(this.hashOperations.size(CAPTCHA_SESSION_KEY));
    }

    @Override
    public Collection getKeys() {
        CAPTCHA_LOG.debug("Get captcha keys.");
        return this.hashOperations.keys(CAPTCHA_SESSION_KEY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void empty() {
        Collection<String> keys = this.getKeys();
        if (!keys.isEmpty()) {
            CAPTCHA_LOG.debug("Empty captcha.");
            this.hashOperations.delete(CAPTCHA_SESSION_KEY, keys.toArray());
        }
    }

    @Override
    public void initAndStart() {
        CAPTCHA_LOG.debug("InitAndStart captcha.");
        this.empty();
    }

    @Override
    public void cleanAndShutdown() {
        CAPTCHA_LOG.debug("CleanAndShutdown captcha.");
        this.empty();
    }

    @SuppressWarnings("unchecked")
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

}
