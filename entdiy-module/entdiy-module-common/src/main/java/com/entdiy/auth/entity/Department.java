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
import com.entdiy.core.entity.BaseNativeNestedSetEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

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
public class Department extends BaseNativeNestedSetEntity<Department> {

    private static final long serialVersionUID = -7634994834209530394L;

    @MetaData(value = "代码", comments = "代码本身具有层级信息，用于进行从属权限控制")
    @Size(min = 3)
    @Column(nullable = false, length = 255, unique = true)
    private String code;

    @MetaData(value = "名称")
    @Column(nullable = false, length = 32)
    private String name;

    @Override
    @Transient
    public String getDisplay() {
        if (code == null) {
            return null;
        }
        return code + " " + name;
    }
}
