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
package com.entdiy.support.weixin.service;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WxPayService extends WxPayServiceImpl {

    /**
     * 设置微信公众号的appid
     */
    @Value("${weixin.appId}")
    private String appId;

    /**
     * 微信支付商户号
     */
    @Value("${weixin.mchId}")
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    @Value("${weixin.mchKey}")
    private String mchKey;

    /**
     * 微信商户证书
     */
    @Value("${weixin.key.path}")
    private String keyPath;

    @PostConstruct
    public void init() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);

        this.setConfig(payConfig);
    }
}
