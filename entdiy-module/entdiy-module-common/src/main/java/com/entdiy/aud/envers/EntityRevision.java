/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.aud.envers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class EntityRevision {

    private static Logger logger = LoggerFactory.getLogger(EntityRevision.class);

    /**
     * The first element will be the changed entity instance.
     */
    private Object entity;

    /**
     * The second will be an entity containing revision data
     * (if no custom entity is used, this will be an instance of DefaultRevisionEntity)
     */
    private ExtDefaultRevisionEntity revisionEntity;

    /**
     * The third will be the type of the revision
     * (one of the values of the RevisionType enumeration: ADD, MOD, DEL).
     */
    private RevisionType revisionType;

    @JsonIgnore
    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public ExtDefaultRevisionEntity getRevisionEntity() {
        return revisionEntity;
    }

    public void setRevisionEntity(ExtDefaultRevisionEntity revisionEntity) {
        this.revisionEntity = revisionEntity;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    @SuppressWarnings("rawtypes")
    @JsonIgnore
    public List<RevEntityProperty> getRevEntityProperties() {
        List<RevEntityProperty> revEntityProperties = new ArrayList<RevEntityProperty>();
        Method[] methods = entity.getClass().getMethods();
        Set<String> excludeBaseProperties = Sets.newHashSet();
        for (Method method : methods) {
            String methodName = method.getName();
            if (!methodName.startsWith("get") || "getClass".equals(methodName) || "getVersion".equals(methodName)
                    || "getOptlock".equals(methodName)) {
                continue;
            }

            //排除非持久属性方法
            if (method.getAnnotation(Transient.class) != null) {
                continue;
            }

            //排除基类方法
            boolean skipMethod = false;
            for (String excludeProperty : excludeBaseProperties) {
                if (("get" + StringUtils.capitalize(excludeProperty)).equals("getOptlock")) {
                    skipMethod = true;
                    break;
                }
            }
            if (skipMethod) {
                continue;
            }

            try {
                Object entityFieldValue = method.invoke(entity);
                if (entityFieldValue instanceof Collection) {
                    Collection items = (Collection) entityFieldValue;
                    if (CollectionUtils.isNotEmpty(items)) {
                        String propertyClass = null;
                        List<Serializable> ids = Lists.newArrayList();
                        for (Object object : items) {
                            if (object instanceof Persistable) {
                                propertyClass = object.getClass().getSimpleName();
                                ids.add(((Persistable) object).getId());
                            }
                        }
                        if (ids.size() > 0) {
                            entityFieldValue = propertyClass + "[" + StringUtils.join(ids, ",") + "]";
                        }
                    }
                }
                RevEntityProperty item = new RevEntityProperty();
                item.setPropertyName(StringUtils.uncapitalize(StringUtils.substring(method.getName(), 3)));
                item.setPropertyValue(entityFieldValue);
                revEntityProperties.add(item);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return revEntityProperties;
    }

    public static class RevEntityProperty {
        private String propertyName;
        private Object propertyValue;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }

        public void setPropertyValue(Object propertyValue) {
            this.propertyValue = propertyValue;
        }

    }
}
