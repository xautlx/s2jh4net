package lab.s2jh.core.security;

import lab.s2jh.core.annotation.MetaData;

import org.apache.shiro.authc.UsernamePasswordToken;

public class SourceUsernamePasswordToken extends UsernamePasswordToken {

    public SourceUsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    private static final long serialVersionUID = 4494958942452530263L;

    @MetaData(value = "登录来源", comments = "标识是前端用户或后端管理等登录来源，可根据不同来源授予默认角色")
    private AuthSourceEnum source;

    public static enum AuthSourceEnum {

        @MetaData(value = "APP手机应用")
        P,

        @MetaData(value = "HTML5 Mobile站点")
        M,

        @MetaData(value = "WWW主站", comments = "source来源为空也表示此类型")
        W,

        @MetaData(value = "Admin管理端")
        A;
    }

    public AuthSourceEnum getSource() {
        return source;
    }

    public void setSource(AuthSourceEnum source) {
        this.source = source;
    }
}
