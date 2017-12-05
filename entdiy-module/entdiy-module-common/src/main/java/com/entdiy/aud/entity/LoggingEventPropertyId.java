package com.entdiy.aud.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/** 
 * 基于logback的DBAppender表结构规范对应的实体定义
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

    @Column(name = "mapped_key", nullable = false, length = 2000)
    private String mappedKey;

    public LoggingEventPropertyId() {
    }

    public LoggingEventPropertyId(Long eventId, String mappedKey) {
        this.eventId = eventId;
        this.mappedKey = mappedKey;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof LoggingEventPropertyId))
            return false;
        LoggingEventPropertyId castOther = (LoggingEventPropertyId) other;

        return ((this.getEventId() == castOther.getEventId()) || (this.getEventId() != null && castOther.getEventId() != null && this.getEventId()
                .equals(castOther.getEventId())))
                && ((this.getMappedKey() == castOther.getMappedKey()) || (this.getMappedKey() != null && castOther.getMappedKey() != null && this
                        .getMappedKey().equals(castOther.getMappedKey())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getEventId() == null ? 0 : this.getEventId().hashCode());
        result = 37 * result + (getMappedKey() == null ? 0 : this.getMappedKey().hashCode());
        return result;
    }

}
