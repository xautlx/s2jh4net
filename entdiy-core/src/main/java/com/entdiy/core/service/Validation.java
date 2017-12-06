package com.entdiy.core.service;

import com.entdiy.core.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 用于业务逻辑校验的“断言”控制，与常规的Assert断言区别在于抛出 @see ValidationException
 * 此类异常不会进行常规的logger.error记录，一般只在前端显示提示用户
 */
public class Validation {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ValidationException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }

    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ValidationException(message);
        }
    }

    /**
     * 如果是演示模式并且是POST请求，则拒绝以避免用户随意修改数据导致演示不完整
     * @param request
     */
    public static void notDemoMode(HttpServletRequest request) {
        if (GlobalConfigService.isDemoMode()) {
            if("POST".equalsIgnoreCase(request.getMethod())){
                throw new ValidationException("抱歉，此功能在演示模式被禁用，请参考文档在本地部署运行体验。");
            }
        }
    }
}
