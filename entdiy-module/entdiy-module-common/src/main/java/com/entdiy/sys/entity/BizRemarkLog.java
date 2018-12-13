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
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * 提供一个通用的为业务数据添加备注流水记录的方式
 * 一般数据量不大、无太多明确业务用途的流水备注可以采用此方式
 */
@Getter
@Setter
@Accessors
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_BizRemarkLog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ApiModel(value = "业务对象备注记录")
public class BizRemarkLog extends BaseNativeEntity {

    @MetaData(value = "业务实体类完整名")
    @Column(length = 256)
    @JsonView(JsonViews.Admin.class)
    private String bizEntityClass;

    @MetaData(value = "业务实体主键ID")
    @Column(length = 64)
    @JsonView(JsonViews.Admin.class)
    private String bizEntityId;

    @ApiModelProperty(value = "备注")
    @JsonView(JsonViews.Admin.class)
    @Column(length = 1000, nullable = false)
    private String remarkLog;

    @MetaData(value = "提交时间")
    @Column(nullable = false)
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime submitTime;
}


