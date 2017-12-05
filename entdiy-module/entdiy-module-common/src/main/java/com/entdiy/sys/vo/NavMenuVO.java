package com.entdiy.sys.vo;

import com.entdiy.core.annotation.MetaData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
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

    @MetaData(value = "菜单层级")
    private Integer level;

    @MetaData(value = "菜单URL")
    private String url;

    @MetaData(value = "图标样式")
    private String style;

    @MetaData(value = "展开标识", tooltips = "是否默认展开菜单组")
    private Boolean initOpen = Boolean.FALSE;
}
