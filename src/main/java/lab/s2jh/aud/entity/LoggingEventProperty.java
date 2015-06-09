package lab.s2jh.aud.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** 
 * 基于logback的DBAppender表结构规范对应的实体定义
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "logging_event_property")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class LoggingEventProperty implements java.io.Serializable {

    private static final long serialVersionUID = -4730407775407355843L;

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "eventId", column = @Column(name = "event_id", nullable = false)),
            @AttributeOverride(name = "mappedKey", column = @Column(name = "mapped_key", nullable = false, length = 254)) })
    private LoggingEventPropertyId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    private LoggingEvent loggingEvent;

    @Column(name = "mapped_value", length = 3000)
    private String mappedValue;
}
