/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler" }, ignoreUnknown = true)
@MappedSuperclass
@AuditOverrides({ @AuditOverride(forClass = BaseEntity.class) })
public abstract class BaseEntity<ID extends Serializable> extends PersistableEntity<ID> {

    private static final long serialVersionUID = 2476761516236455260L;

    /** 乐观锁版本,初始设置为0 */
    private Integer version = 0;

    public abstract void setId(final ID id);

    @Version
    @Column(name = "optlock", nullable = false)
    @JsonProperty
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

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
    public String getDisplay() {
        return "[" + getId() + "]" + this.getClass().getSimpleName();
    }
}
