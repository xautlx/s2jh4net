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
package com.entdiy.security.api;

import com.entdiy.core.exception.ValidationException;
import com.entdiy.core.service.Validation;
import com.entdiy.core.web.AppContextHolder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class ClientValidationAuthenticationFilter extends PathMatchingFilter {

    private static final Logger logger = LoggerFactory.getLogger(ClientValidationAuthenticationFilter.class);

    public static final String HEADER_NAME_SIGN = "sign";

    @Getter
    @Setter
    private Properties appKeySecrets = new Properties();

    /**
     * 从Request请求从参数或Header中提取参数值
     *
     * @param request
     * @param key
     * @return
     */
    private String getValidationValue(ServletRequest request, String key) {
        HttpServletRequest req = (HttpServletRequest) request;
        String value = req.getHeader(key);
        if (value == null) {
            value = req.getParameter(key);
        }
        Validation.notBlank(value, "Request parameter or header '" + key + "' is required");
        return value;
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        String message;
        try {
            /**
             * 对请求参数做鉴权校验，目前主要实现请求客户端来源的合法性校验。
             * 可以进一步优化追加timestamp和nonce的防重放和拦截攻击等支持。
             * 可以进一步优化追加重要业务参数加入到签名，以防业务数据篡改和防抵赖支持。
             */
            String sign = getValidationValue(request, HEADER_NAME_SIGN);
            //在开发模式并且sign值为dev，直接放行以便Restful接口开发调试
            if (AppContextHolder.isDevMode() && "dev".equalsIgnoreCase(sign)) {
                return true;
            }

            String appkey = getValidationValue(request, "appkey");
            String timestamp = getValidationValue(request, "timestamp");
            //客户端提供的此值建议保持足够的随机性，可以考虑使用UUID之类的全局唯一值
            String nonce = getValidationValue(request, "nonce");


            String secret = (String) appKeySecrets.get(appkey);
            Validation.notBlank(secret, "Invalid appkey: " + appkey);

            //加密签名规则，主要客户端需要与此规则保持完全一致
            String str = "{" + secret + "}timestamp=" + timestamp + "&nonce=" + nonce;
            String encode = DigestUtils.sha1Hex(str);

            Validation.isTrue(encode.equalsIgnoreCase(sign), "Sign value invalid");

            return true;
        } catch (ValidationException e) {
            message = e.getMessage();
        } catch (Exception e) {
            message = "API Client Validation Error";
            logger.error(message, e);
        }

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        IOUtils.write(message, httpResponse.getOutputStream());
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}