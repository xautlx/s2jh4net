package lab.s2jh.module.sys.vo;

import java.io.Serializable;

import lab.s2jh.core.annotation.MetaData;

import org.apache.shiro.aop.MethodInvocation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NavMenuVO implements Serializable {

    private static final long serialVersionUID = 9047695739997529718L;

    @MetaData(value = "菜单路径")
    private String path;

    @MetaData(value = "菜单URL")
    private String url;

    @MetaData(value = "图标样式")
    private String style;

    @MetaData(value = "展开标识", tooltips = "是否默认展开菜单组")
    private Boolean initOpen = Boolean.FALSE;

    private MethodInvocation methodInvocation;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @JsonIgnore
    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public void setMethodInvocation(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    public Boolean getInitOpen() {
        return initOpen;
    }

    public void setInitOpen(Boolean initOpen) {
        this.initOpen = initOpen;
    }
}
