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
package com.entdiy.core.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.aud.DefaultAuditable;
import com.entdiy.core.aud.SaveUpdateAuditListener;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Access(AccessType.FIELD)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler"}, ignoreUnknown = true)
@MappedSuperclass
@EntityListeners({SaveUpdateAuditListener.class})
@AuditOverrides({@AuditOverride(forClass = BaseEntity.class)})
public abstract class BaseEntity<ID extends Serializable> extends AbstractPersistableEntity<ID> implements DefaultAuditable {

    private static final long serialVersionUID = 2476761516236455260L;

    @MetaData(value = "乐观锁版本")
    @Version
    @Column(name = "_version", nullable = false)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    private Integer version = 0;

    @Column(name = "_createUserName", length = 256, updatable = false)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    private String createUserName = GlobalConstant.NONE_VALUE;

    @Column(name = "_createAccountId", length = 256, updatable = false)
    @JsonIgnore
    private Long createAccountId;

    @JsonProperty
    @Column(name = "_createDate", updatable = false)
    protected LocalDateTime createDate;

    @Column(name = "_updateUserName", length = 256)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    private String updateUserName;

    @Column(name = "_updateAccountId", length = 256)
    @JsonIgnore
    private Long updateAccountId;

    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @Column(name = "_updateDate")
    private LocalDateTime updateDate;

    @MetaData(value = "数据隔离域", comments = "类似Windows域的概念，进行用户数据隔离")
    @Column(name = "_dataDomain", length = 100, updatable = false)
    @JsonView(JsonViews.Admin.class)
    private String dataDomain = GlobalConstant.DEFAULT_VALUE;

    private static final String[] PROPERTY_LIST = new String[]{"id", "version"};

    public String[] retriveCommonProperties() {
        return PROPERTY_LIST;
    }

    @Override
    @Transient
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    public String getDisplay() {
        return "[" + getId() + "]" + this.getClass().getSimpleName();
    }
}
