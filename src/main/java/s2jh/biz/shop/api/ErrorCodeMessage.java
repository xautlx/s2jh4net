package s2jh.biz.shop.api;

public enum ErrorCodeMessage {

    E100("请求客户端IP不合法"),

    E110("method操作标识不能为空"),

    E111("method操作标识无效: %s"),

    E112("缺少必须的参数：%s"),

    E140("appKey客户端标识不能为空"),

    E222("业务处理异常: %s"),

    E223("账号未登录"),

    E999("未定义异常错误: %s");

    private String message;

    private ErrorCodeMessage(String message) {
        this.message = message;
    }

    public ErrorCodeMessage format(Object... params) {
        this.message = String.format(this.message, params);
        return this;
    }

    public String getMessage() {
        return message;
    }
}
