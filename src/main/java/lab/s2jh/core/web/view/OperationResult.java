/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.web.view;

import lab.s2jh.core.annotation.MetaData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 用于Object到JSON序列化的对象结构体定义
 */
@JsonInclude(Include.NON_NULL)
public class OperationResult {

    /** 标识操作结果类型 */
    public enum OPERATION_RESULT_TYPE {
        @MetaData(value = "成功", comments = "操作处理成功。前端一般是绿色的短暂气泡提示")
        success,

        @MetaData(value = "警告", comments = "偶尔用于标识业务处理基本完成，但是其中存在一些需要注意放在message或userdata中的提示信息。前端一般是黄色的气泡提示")
        warning,

        @MetaData(value = "失败", comments = "操作处理失败。前端一般是红色的长时间或需要用户主动关闭的气泡提示")
        failure,

        @MetaData(value = "确认", comments = "本次提交中止，反馈用户进行确认。前端一般会弹出一个供用户'确认'操作的对话框，然后用户主动确认之后会自动再次发起请求并跳过确认检查进行后续业务处理")
        confirm
    }

    /** 返回success或failure操作标识 */
    private String type;

    /** 国际化处理的返回JSON消息正文 */
    private String message;

    /** 标识redirect路径 */
    private String redirect;

    /** 补充的数据 */
    private Object userdata;

    public static OperationResult buildSuccessResult(String message, Object userdata) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message, userdata);
    }

    public static OperationResult buildSuccessResult() {
        return new OperationResult(OPERATION_RESULT_TYPE.success, null);
    }

    public static OperationResult buildSuccessResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message);
    }

    public static OperationResult buildSuccessResult(Object userdata) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, "success", userdata);
    }

    public static OperationResult buildWarningResult(String message, Object userdata) {
        return new OperationResult(OPERATION_RESULT_TYPE.warning, message, userdata);
    }

    public static OperationResult buildFailureResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.failure, message);
    }

    public static OperationResult buildFailureResult(String message, Object userdata) {
        return new OperationResult(OPERATION_RESULT_TYPE.failure, message, userdata);
    }

    public static OperationResult buildConfirmResult(String message, Object userdata) {
        return new OperationResult(OPERATION_RESULT_TYPE.confirm, message, userdata);
    }

    public static OperationResult buildConfirmResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.confirm, message, null);
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message) {
        this.type = type.name();
        this.message = message;
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message, Object userdata) {
        this.type = type.name();
        this.message = message;
        this.userdata = userdata;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getUserdata() {
        return userdata;
    }

    public void setUserdata(Object userdata) {
        this.userdata = userdata;
    }

    public OperationResult setRedirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getRedirect() {
        return redirect;
    }
}
