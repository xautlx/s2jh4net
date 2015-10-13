package lab.s2jh.core.exception;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.NestedRuntimeException;

public abstract class BaseRuntimeException extends NestedRuntimeException {

    private static final long serialVersionUID = -23347847086757165L;

    private String errorCode;

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    //生成一个异常流水号，追加到错误消息上显示到前端用户，用户反馈问题时给出此流水号给运维或开发人员快速定位对应具体异常细节
    public static String buildExceptionCode() {
        return "ERR" + DateFormatUtils.format(new java.util.Date(), "yyMMddHHmmss") + RandomStringUtils.randomNumeric(3);
    }
}
