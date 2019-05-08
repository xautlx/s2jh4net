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
package com.entdiy.aud.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 基于logback的DBAppender表结构规范对应的实体定义
 *
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Embeddable
public class LoggingEventPropertyId implements java.io.Serializable {

    private static final long serialVersionUID = -8453417940199653002L;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "mapped_key", nullable = false, length = 128)
    private String mappedKey;

    public LoggingEventPropertyId() {
    }

    public LoggingEventPropertyId(Long eventId, String mappedKey) {
        this.eventId = eventId;
        this.mappedKey = mappedKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggingEventPropertyId that = (LoggingEventPropertyId) o;

        if (!eventId.equals(that.eventId)) return false;
        return mappedKey.equals(that.mappedKey);
    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + mappedKey.hashCode();
        return result;
    }
}
