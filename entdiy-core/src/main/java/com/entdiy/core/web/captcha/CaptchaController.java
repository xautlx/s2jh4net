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

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.web.view.OperationResult;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * JCaptcha验证码Controller
 */
@Controller
public class CaptchaController {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired(required = false)
    private GenericManageableCaptchaService captchaService;

    @MetaData(value = "生成验证码")
    @RequestMapping(value = "/pub/captcha", method = RequestMethod.GET)
    public void genCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (captchaService == null) {
            logger.warn("CaptchaService instance NOT found.");
            return;
        }

        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        String captchaId = CaptchaUtils.getCaptchaID(request);
        logger.debug("Generatin captch image for: {}", captchaId);


        // create the image with the text
        BufferedImage bi = captchaService.getImageChallengeForID(captchaId);

        // write the data out
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        out.flush();
        out.close();
    }

    /**
     * @see DoubleCheckableCaptchaService#touchValidateResponseForID(String, Object)
     */
    @MetaData(value = "预校验验证码")
    @RequestMapping(value = "/pub/captcha/touch-validate", method = RequestMethod.GET)
    @ResponseBody
    public Boolean touchValidateCaptcha(HttpServletRequest request, HttpServletResponse response) {
        return CaptchaUtils.touchValidateCaptchaCode(request, "captcha");
    }

    /**
     * 供APP端统一结构返回调用
     *
     * @see DoubleCheckableCaptchaService#touchValidateResponseForID(String, Object)
     */
    @MetaData(value = "预校验验证码")
    @RequestMapping(value = "/api/pub/captcha/touch-validate", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult touchValidateCaptchaApi(HttpServletRequest request, HttpServletResponse response) {
        return OperationResult.buildSuccessResult(CaptchaUtils.touchValidateCaptchaCode(request, "captcha"));
    }
}