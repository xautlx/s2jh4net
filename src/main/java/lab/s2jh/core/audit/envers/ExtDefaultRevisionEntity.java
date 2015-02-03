package lab.s2jh.core.audit.envers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 扩展默认的Hibernate Envers审计表对象定义
 * 
 * @see http://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html/ch15.html
 */
@Entity
@Table(name = "AUD_RevisionEntity")
@RevisionEntity(ExtRevisionListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler" }, ignoreUnknown = true)
public class ExtDefaultRevisionEntity {
    /** 记录版本 */
    private Long rev;

    /** 记录时间 */
    private Date revstmp;

    /** 请求执行的Web Controller类名 */
    private String controllerClassName;

    /** 请求执行的Web Controller方法名 */
    private String controllerMethodName;

    /** 全局唯一的用户ID，确保明确与唯一操作用户关联 */
    private String authGuid;

    /** 用户信息友好显示字符 */
    private String authDisplay;

    @Id
    @GeneratedValue
    @RevisionNumber
    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getRevstmp() {
        return revstmp;
    }

    public void setRevstmp(Date revstmp) {
        this.revstmp = revstmp;
    }

    @Column(length = 128)
    public String getAuthGuid() {
        return authGuid;
    }

    public void setAuthGuid(String authGuid) {
        this.authGuid = authGuid;
    }

    @Column(length = 256)
    public String getAuthDisplay() {
        return authDisplay;
    }

    public void setAuthDisplay(String authDisplay) {
        this.authDisplay = authDisplay;
    }

    @Column(length = 256)
    public String getControllerClassName() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName = controllerClassName;
    }

    @Column(length = 256)
    public String getControllerMethodName() {
        return controllerMethodName;
    }

    public void setControllerMethodName(String controllerMethodName) {
        this.controllerMethodName = controllerMethodName;
    }

}
