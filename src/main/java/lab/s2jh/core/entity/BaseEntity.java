/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.DefaultAuditable;
import lab.s2jh.core.audit.SaveUpdateAuditListener;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lab.s2jh.core.web.json.JsonViews;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Access(AccessType.FIELD)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler" }, ignoreUnknown = true)
@MappedSuperclass
@EntityListeners({ SaveUpdateAuditListener.class })
@AuditOverrides({ @AuditOverride(forClass = BaseEntity.class) })
public abstract class BaseEntity<ID extends Serializable> extends PersistableEntity<ID> implements DefaultAuditable<String, ID> {

    private static final long serialVersionUID = 2476761516236455260L;

    @MetaData(value = "乐观锁版本")
    @Version
    @Column(name = "optlock", nullable = false)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    private Integer version = 0;

    @Column(length = 100)
    @JsonIgnore
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty
    protected Date createdDate;

    @Column(length = 100)
    @JsonIgnore
    private String lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonIgnore
    private Date lastModifiedDate;

    public abstract void setId(final ID id);

    @Transient
    public void resetCommonProperties() {
        setId(null);
        version = 0;
        addExtraAttribute(PersistableEntity.EXTRA_ATTRIBUTE_DIRTY_ROW, true);
    }

    private static final String[] PROPERTY_LIST = new String[] { "id", "version" };

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
