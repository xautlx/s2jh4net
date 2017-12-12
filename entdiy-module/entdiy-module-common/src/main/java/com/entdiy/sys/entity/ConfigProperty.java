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
package com.entdiy.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.web.json.JsonViews;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonView;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_ConfigProperty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "配置属性")
@Audited
public class ConfigProperty extends BaseNativeEntity {

    private static final long serialVersionUID = 8136580659799847607L;

    @MetaData(value = "代码")
    @Column(length = 64, unique = true, nullable = false)
    private String propKey;

    @MetaData(value = "名称")
    @Column(length = 256, nullable = false)
    private String propName;

    @MetaData(value = "简单属性值")
    @Column(length = 256)
    private String simpleValue;

    @MetaData(value = "HTML属性值")
    @Lob
    @JsonView(JsonViews.AppDetail.class)
    private String htmlValue;

    @MetaData(value = "参数属性用法说明")
    @Column(length = 2000)
    private String propDescn;

    @Override
    @Transient
    public String getDisplay() {
        return propKey;
    }
}
