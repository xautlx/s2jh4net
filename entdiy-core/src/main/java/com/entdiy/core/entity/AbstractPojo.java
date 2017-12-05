package com.entdiy.core.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

public abstract class AbstractPojo implements Serializable {

    private static final long serialVersionUID = 1404395927402847666L;

    /** Entity本身无用，主要用于UI层辅助参数传递 */
    @Transient
    @JsonProperty
    protected Map<String, Object> extraAttributes;

    @Transient
    public void addExtraAttribute(String key, Object value) {
        if (this.extraAttributes == null) {
            this.extraAttributes = Maps.newHashMap();
        }
        this.extraAttributes.put(key, value);
    }

    @Transient
    public void addExtraAttributes(Map<String, Object> extraAttributes) {
        if (this.extraAttributes == null) {
            this.extraAttributes = Maps.newHashMap();
        }
        this.extraAttributes.putAll(extraAttributes);
    }

    @Transient
    @JsonIgnore
    public String getExtraAttributesValue(String key) {
        if (extraAttributes == null) {
            return null;
        }
        Object opParams = extraAttributes.get(key);
        if (opParams == null) {
            return null;
        }
        String op = null;
        if (opParams instanceof String[]) {
            op = ((String[]) opParams)[0];
        } else if (opParams instanceof String) {
            op = (String) opParams;
        }
        return op;
    }

    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }
}