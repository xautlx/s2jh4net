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

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.entdiy.auth.entity.User;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.web.json.DateTimeJsonSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_NotifyMessageRead", uniqueConstraints = @UniqueConstraint(columnNames = { "notifyMessage_id", "readUser_id" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@MetaData(value = "公告阅读记录")
public class NotifyMessageRead extends BaseNativeEntity {

    private static final long serialVersionUID = -2680515888038751963L;

    @MetaData(value = "公告")
    @ManyToOne
    @JoinColumn(name = "notifyMessage_id", nullable = false)
    @JsonIgnore
    private NotifyMessage notifyMessage;

    @MetaData(value = "阅读用户")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "readUser_id", nullable = false)
    @JsonIgnore
    private User readUser;

    @MetaData(value = "首次阅读时间")
    @Column(nullable = false, updatable = false)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    private Date firstReadTime;

    @MetaData(value = "最后阅读时间")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    private Date lastReadTime;

    @MetaData(value = "总计阅读次数")
    @Column(nullable = false)
    private Integer readTotalCount = 1;

    @Override
    @Transient
    public String getDisplay() {
        return null;
    }

    @Transient
    public String getReadUserLabel() {
        return readUser.getDisplay();
    }
}
