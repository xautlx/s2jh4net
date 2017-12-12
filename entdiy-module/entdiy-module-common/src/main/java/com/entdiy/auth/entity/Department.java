/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.auth.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Department")
@MetaData(value = "部门")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class Department extends BaseNativeEntity implements Comparable<Department> {

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
    @JsonIgnore
    private Department parent;

    @Override
    @Transient
    public String getDisplay() {
        if (code == null) {
            return null;
        }
        return code + " " + name;
    }

    @Transient
    public String getPathDisplay() {
        Department category = this;
        String label = this.getName();
        while (category.getParent() != null) {
            label = category.getParent().getName() + "/" + label;
            category = category.getParent();
        }
        return label;
    }

    /**
     * 计算节点所在层级，根节点以0开始
     * @return
     */
    @Transient
    @JsonIgnore
    public int getLevel() {
        int level = 0;
        return loopLevel(level, this);
    }

    private int loopLevel(int level, Department item) {
        Department parent = item.getParent();
        if (parent != null && parent.getId() != null) {
            return loopLevel(level + 1, item.getParent());
        }
        return level;
    }

    @Override
    public int compareTo(Department o) {
        return CompareToBuilder.reflectionCompare(o.getCode(), this.getCode());
    }
}
