package com.entdiy.core.exception;

/**
 * 业务逻辑校验异常，此类异常不会进行常规的logger.error记录，一般只在前端显示提示用户
 */
public class ValidationException extends BaseRuntimeException {

    private static final long serialVersionUID = -1613416718940821955L;

    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationException(String message) {
        super(message);
    }
}
