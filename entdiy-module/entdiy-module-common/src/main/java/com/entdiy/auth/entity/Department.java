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
package com.entdiy.auth.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.entity.TreeDataDto;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Department")
@MetaData(value = "部门")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class Department extends BaseNativeEntity {

    private static final long serialVersionUID = -7634994834209530394L;

    @MetaData(value = "代码", comments = "代码本身具有层级信息，用于进行从属权限控制")
    @Size(min = 3)
    @Column(nullable = false, length = 255, unique = true)
    private String code;

    @MetaData(value = "名称")
    @Column(nullable = false, length = 32)
    private String name;

    @MetaData(value = "父节点")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "none"))
    @JsonView(JsonViews.Tree.class)
    private Department parent;

    @MetaData(value = "排序号", tooltips = "相对排序号，数字越大越靠上显示")
    @JsonView(JsonViews.Admin.class)
    private Integer orderRank = 10;

    @MetaData(value = "禁用标识", tooltips = "禁用项目用户端不显示")
    @JsonView(JsonViews.Admin.class)
    private Boolean disabled = Boolean.FALSE;

    @Formula("(select count(*) from auth_Department d where d.parent_id = id)")
    @Basic(fetch = FetchType.LAZY)
    @NotAudited
    private Integer enabledChildrenCount;

    @Override
    @Transient
    public String getDisplay() {
        if (code == null) {
            return null;
        }
        return code + " " + name;
    }

    public interface DepartmentTreeDataDto extends TreeDataDto {
        String getCode();

        String getName();

        default String getDisplay() {
            return getCode().concat(" ").concat(getName());
        }
    }
}
