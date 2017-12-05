package com.entdiy.core.web.captcha;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.web.view.OperationErrCode;
import com.entdiy.core.web.view.OperationResult;
import com.octo.captcha.service.CaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * JCaptcha验证
 */
public class CaptchaUtils {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaUtils.class);

    private static CaptchaService captchaService;

    public static OperationResult validateCaptchaCode(HttpServletRequest request, String captchaParamName) {
        try {
            String captcha = request.getParameter(captchaParamName);
            if (StringUtils.isBlank(captcha)) {
                return OperationResult.buildFailureResult("验证码不能为空").setCode(OperationErrCode.CAPTCHA_INVALID);
            }

            //兼容APP请求，基于AccessToken作为验证码标识，否则就按照标准的Session标识
            String captchaId = request.getHeader(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
            if (StringUtils.isBlank(captchaId)) {
                captchaId = request.getSession().getId();
            }

            captchaService = captchaService == null ? SpringContextHolder.getBean(CaptchaService.class) : captchaService;
            logger.debug("Validating captcha code: {}", captcha);
            if (!captchaService.validateResponseForID(captchaId, captcha)) {
                return OperationResult.buildFailureResult("验证码不正确").setCode(OperationErrCode.CAPTCHA_INVALID);
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.warn(e.getMessage(), e);
            }
            return OperationResult.buildFailureResult("验证码已失效，请刷新验证码").setCode(OperationErrCode.CAPTCHA_INVALID);
        }
        return null;
    }
}