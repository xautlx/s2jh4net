package lab.s2jh.aud.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** 
 * 基于logback的DBAppender表结构规范对应的实体定义
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Entity
@Table(name = "logging_event_exception")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class LoggingEventException implements java.io.Serializable {

    private static final long serialVersionUID = -1773975388851920530L;

    private LoggingEventExceptionId id;
    private LoggingEvent loggingEvent;
    private String traceLine;

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "eventId", column = @Column(name = "event_id", nullable = false, precision = 38, scale = 0)),
            @AttributeOverride(name = "i", column = @Column(name = "i", nullable = false)) })
    public LoggingEventExceptionId getId() {
        return this.id;
    }

    public void setId(LoggingEventExceptionId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    public LoggingEvent getLoggingEvent() {
        return this.loggingEvent;
    }

    public void setLoggingEvent(LoggingEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
    }

    @Column(name = "trace_line", nullable = false, length = 4000)
    public String getTraceLine() {
        return this.traceLine;
    }

    public void setTraceLine(String traceLine) {
        this.traceLine = traceLine;
    }

}
