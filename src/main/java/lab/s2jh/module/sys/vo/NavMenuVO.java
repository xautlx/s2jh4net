package lab.s2jh.module.sys.vo;

import java.io.Serializable;

import lab.s2jh.core.annotation.MetaData;

public class NavMenuVO implements Serializable {

    private static final long serialVersionUID = 9047695739997529718L;

    @MetaData(value = "编号")
    private Long id;

    @MetaData(value = "父编号")
    private Long parentId;

    @MetaData(value = "菜单名称")
    private String name;

    @MetaData(value = "菜单路径")
    private String path;

    @MetaData(value = "菜单URL")
    private String url;

    @MetaData(value = "图标样式")
    private String style;

    @MetaData(value = "展开标识", tooltips = "是否默认展开菜单组")
    private Boolean initOpen = Boolean.FALSE;

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

    public Boolean getInitOpen() {
        return initOpen;
    }

    public void setInitOpen(Boolean initOpen) {
        this.initOpen = initOpen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
