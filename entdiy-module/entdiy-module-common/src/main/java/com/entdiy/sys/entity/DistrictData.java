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
package com.entdiy.sys.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.locale.entity.LocalizedLabel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * 三级行政区域数据对象，SQL数据脚本在 resources/data/DistrictData.data
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_DistrictData")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "行政区域数据")
public class DistrictData extends AbstractPersistableEntity<String> {

    @MetaData("主键(区域代码)")
    @Id
    @Column(length = 8)
    @JsonProperty
    @JsonView(JsonViews.Public.class)
    private String id;

    @MetaData(value = "名称")
    @Column(length = 256, nullable = false)
    private LocalizedLabel name;

    @MetaData(value = "父代码")
    @Column(length = 8)
    private String parentId;

    @Override
    @Transient
    public String getDisplay() {
        return name.getLocalizedText();
    }
}
