package com.entdiy.core.web.captcha;

import com.entdiy.core.web.util.ServletUtils;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    // 生成验证码
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

        //兼容APP请求，基于AccessToken作为验证码标识，否则就按照标准的Session标识
        String captchaId = ServletUtils.getOAuthAccessToken(request);
        if (StringUtils.isBlank(captchaId)) {
            captchaId = request.getSession().getId();
        }
        logger.trace("Generatin captch image for: {}", captchaId);

        // create the image with the text
        BufferedImage bi = captchaService.getImageChallengeForID(captchaId);

        // write the data out
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        out.flush();
        out.close();
    }
}