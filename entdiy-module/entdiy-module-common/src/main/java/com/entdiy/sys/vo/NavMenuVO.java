/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.sys.vo;

import com.entdiy.core.annotation.MetaData;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

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

    public Map<String, Object> buildMapDataForTreeDisplay() {
        //组装zTree结构数据
        Map<String, Object> item = Maps.newHashMap();
        item.put("id", this.getId());
        item.put("parent", this.getParentId());
        item.put("name", this.getName());
        item.put("display", this.getName());
        item.put("open", true);
        item.put("enabledChildrenCount", StringUtils.isBlank(this.getUrl()) ? 1 : 0);
        return item;
    }
}
