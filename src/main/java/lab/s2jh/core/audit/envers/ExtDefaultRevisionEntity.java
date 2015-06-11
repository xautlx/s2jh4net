package lab.s2jh.core.audit.envers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lab.s2jh.module.auth.entity.User.AuthTypeEnum;

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
@Table(name = "aud_RevisionEntity")
@RevisionEntity(ExtRevisionListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler" }, ignoreUnknown = true)
public class ExtDefaultRevisionEntity extends PersistableEntity<Long> {

    private static final long serialVersionUID = -2946153158442502361L;

    /** 记录版本 */
    private Long rev;

    /** 记录时间 */
    private Date revstmp;

    private String entityClassName;

    /** Controller注解定义的requestMapping */
    private String requestMappingUri;

    /** 请求执行的Web Controller类名 */
    private String controllerClassName;

    /** 请求执行的Web Controller类MetaData中文注解 */
    private String controllerClassLabel;

    /** 请求执行的Web Controller方法名 */
    private String controllerMethodName;

    /** 请求执行的Web Controller方法的MetaData中文注解 */
    private String controllerMethodLabel;

    /** 请求执行的Web Controller方法RequestMethod: POST */
    private String controllerMethodType;

    /** 全局唯一的用户ID，确保明确与唯一操作用户关联 */
    private String authGuid;

    @MetaData(value = "账号类型所对应唯一标识")
    private String authUid;

    @MetaData(value = "账号类型")
    private AuthTypeEnum authType;

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

    @Column(length = 16)
    public String getControllerMethodType() {
        return controllerMethodType;
    }

    public void setControllerMethodType(String controllerMethodType) {
        this.controllerMethodType = controllerMethodType;
    }

    @Column(length = 256)
    public String getControllerMethodLabel() {
        return controllerMethodLabel;
    }

    public void setControllerMethodLabel(String controllerMethodLabel) {
        this.controllerMethodLabel = controllerMethodLabel;
    }

    @Column(length = 256)
    public String getRequestMappingUri() {
        return requestMappingUri;
    }

    public void setRequestMappingUri(String requestMappingUri) {
        this.requestMappingUri = requestMappingUri;
    }

    @Column(length = 256)
    public String getControllerClassLabel() {
        return controllerClassLabel;
    }

    public void setControllerClassLabel(String controllerClassLabel) {
        this.controllerClassLabel = controllerClassLabel;
    }

    @Column(length = 64)
    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    @Column(length = 8)
    @Enumerated(EnumType.STRING)
    public AuthTypeEnum getAuthType() {
        return authType;
    }

    public void setAuthType(AuthTypeEnum authType) {
        this.authType = authType;
    }

    @Override
    @Transient
    public Long getId() {
        return rev;
    }

    @Override
    @Transient
    public boolean isNew() {
        return rev == null;
    }

    @Override
    @Transient
    public String getDisplay() {
        return String.valueOf(rev);
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }
}
