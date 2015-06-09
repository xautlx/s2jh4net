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
 * @see http://logback.qos.ch/manual/configuration.html#DBAppender
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "logging_event_exception")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class LoggingEventException implements java.io.Serializable {

    private static final long serialVersionUID = -1773975388851920530L;

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "eventId", column = @Column(name = "event_id", nullable = false, precision = 38, scale = 0)),
            @AttributeOverride(name = "i", column = @Column(name = "i", nullable = false)) })
    private LoggingEventExceptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    private LoggingEvent loggingEvent;

    @Column(name = "trace_line", nullable = false, length = 4000)
    private String traceLine;
}
