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

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.exception.ServiceException;
import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * JCaptcha验证
 */
public class CaptchaUtils {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaUtils.class);

    private static DoubleCheckableCaptchaService captchaService;

    /**
     * 获取请求对应的验证码存储标识ID
     * @param request
     * @return
     */
    public static String getCaptchaID(ServletRequest request) {
        String captchaId;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            //兼容APP请求，基于AccessToken作为验证码标识，否则就按照标准的Session标识
            captchaId = httpServletRequest.getHeader(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
            if (StringUtils.isBlank(captchaId)) {
                captchaId = httpServletRequest.getSession().getId();
            }
        } else {
            captchaId = request.getParameter(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
        }
        return captchaId;
    }

    /**
     * 预校验验证码正确性（不会移除当前验证码）
     *
     * @param request          HTTP请求
     * @param captchaParamName 验证码参数名称
     * @retur
     */
    public static boolean touchValidateCaptchaCode(ServletRequest request, String captchaParamName) {
        return validateCaptchaCode(request, captchaParamName, true);
    }

    /**
     * 断言验证码正确性（调用之后无论校验通过与否都会移除当前存储的验证码），一般用在服务端对验证码进行最终校验，如果失败直接抛出运行异常
     *
     * @param request          HTTP请求
     * @param captchaParamName 验证码参数名称
     * @throws CaptchaValidationException
     */
    public static void assetValidateCaptchaCode(ServletRequest request, String captchaParamName) {
        if (!validateCaptchaCode(request, captchaParamName, false)) {
            throw new CaptchaValidationException("Asset captcha code failure");
        }
    }

    /**
     * 验证码校验接口
     *
     * @param request          HTTP请求
     * @param captchaParamName 验证码参数名称
     * @param justTouch        预校验标识 @see DoubleCheckableCaptchaService#touchValidateResponseForID(String, Object)
     * @retur
     */
    private static boolean validateCaptchaCode(ServletRequest request, String captchaParamName, boolean justTouch) {
        try {
            String captcha = request.getParameter(captchaParamName);
            if (StringUtils.isBlank(captcha)) {
                throw new ServiceException("captcha required");
            }
            captcha = captcha.toUpperCase();


            captchaService = captchaService == null ? SpringContextHolder.getBean(DoubleCheckableCaptchaService.class) : captchaService;
            logger.debug("Validating captcha code: {}", captcha);

            String captchaId=CaptchaUtils.getCaptchaID(request);
            if (justTouch) {
                return captchaService.touchValidateResponseForID(captchaId, captcha);
            } else {
                return captchaService.validateResponseForID(captchaId, captcha);
            }
        } catch (CaptchaServiceException e) {
            logger.warn("captcha validate error", e);
            return false;
        }
    }
}