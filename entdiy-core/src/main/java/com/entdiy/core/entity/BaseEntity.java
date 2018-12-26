/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.aud.DefaultAuditable;
import com.entdiy.core.aud.SaveUpdateAuditListener;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.util.reflection.ReflectionUtils;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Access(AccessType.FIELD)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler"}, ignoreUnknown = true)
@MappedSuperclass
@EntityListeners({SaveUpdateAuditListener.class})
@AuditOverrides({@AuditOverride(forClass = BaseEntity.class)})
public abstract class BaseEntity<ID extends Serializable> extends AbstractPersistableEntity<ID> implements DefaultAuditable {

    private static final long serialVersionUID = 2476761516236455260L;

    @MetaData(value = "乐观锁版本")
    @Version
    @Column(name = "_version", nullable = false)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private Integer version = 0;

    @Column(name = "_createUserName", length = 256, updatable = false)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private String createUserName = GlobalConstant.NONE_VALUE;

    @Column(name = "_createAccountId", length = 256, updatable = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long createAccountId;

    @JsonProperty
    @Column(name = "_createDate", updatable = false)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    protected LocalDateTime createDate;

    @Column(name = "_updateUserName", length = 256)
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private String updateUserName;

    @Column(name = "_updateAccountId", length = 256)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long updateAccountId;

    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @Column(name = "_updateDate")
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateDate;

    @MetaData(value = "数据隔离域", comments = "类似Windows域的概念，进行用户数据隔离")
    @Column(name = "_dataDomain", length = 100, updatable = false)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private String dataDomain;

    @MetaData(value = "系统备注说明", comments = "预留一个通用的系统备注字段，业务根据需要合理使用")
    @Column(name = "_systemRemark", length = 1000, updatable = true)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private String systemRemark;

    private static final String[] PROPERTY_LIST = new String[]{"id", "version"};

    public String[] retriveCommonProperties() {
        return PROPERTY_LIST;
    }

    @MetaData(value = "获取已关联对象主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    protected List<ID> getR2TargetIds(String r2Name, String r2TargetName) {
        List r2s = (List) ReflectionUtils.invokeGetterMethod(this, r2Name);
        if (CollectionUtils.isEmpty(r2s)) {
            return null;
        }
        List<ID> targetIds = Lists.newArrayList();
        for (Object r2 : r2s) {
            Persistable target = (Persistable) ReflectionUtils.invokeGetterMethod(r2, r2TargetName);
            targetIds.add((ID) target.getId());
        }
        return targetIds;
    }

    @MetaData(value = "设置已关联对象主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    protected void setR2TargetIds(String r2Name, String r2TargetName, String r2ThisName, ID... ids) {
        final List<ID> r2TargetIds = (ids != null && ids.length > 0) ? Lists.newArrayList(ids) : Lists.newArrayList();
        //没有关联对象集合，清空关联集合
        Method r2Method = ReflectionUtils.getGetterMethod(this.getClass(), r2Name);
        List r2s;
        try {
            r2s = (List) r2Method.invoke(this);
        } catch (Exception e) {
            throw new ServiceException("R2 method invoke error", e);
        }
        if (CollectionUtils.isEmpty(r2TargetIds) && CollectionUtils.isNotEmpty(r2s)) {
            r2s.clear();
        } else {
            //有关联对象集合，如果原来没有则初始化创建，否则移除不存在关联
            if (r2s == null) {
                r2s = Lists.newArrayList();
                ReflectionUtils.invokeSetterMethod(this, r2Name, r2s);
            } else {
                r2s.removeIf(r2 -> !r2TargetIds.stream().anyMatch(r2TargetId -> {
                    Persistable target = (Persistable) ReflectionUtils.invokeGetterMethod(r2, r2TargetName);
                    return r2TargetId.equals(target.getId());
                }));
            }
            //先移除已经存在的关键对象主键，然后追加剩余新增关联
            final List finalR2s = r2s;
            r2TargetIds.removeIf(r2TargetId -> finalR2s.stream().anyMatch(r2 -> {
                Persistable target = (Persistable) ReflectionUtils.invokeGetterMethod(r2, r2TargetName);
                return target.getId().equals(r2TargetId);
            }));
            r2TargetIds.forEach(r2TargetId -> {
                try {
                    Type genericReturnType = r2Method.getGenericReturnType();
                    Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                    Class r2Class = Class.forName(actualTypeArguments[0].getTypeName());
                    Object r2 = r2Class.newInstance();
                    ReflectionUtils.invokeSetterMethod(r2, r2ThisName, this);
                    Method r2TargetMethod = ReflectionUtils.getGetterMethod(r2Class, r2TargetName);
                    Type r2TargetType = r2TargetMethod.getReturnType();
                    Object r2Target = Class.forName(r2TargetType.getTypeName()).newInstance();
                    ReflectionUtils.invokeSetterMethod(r2Target, "id", r2TargetId);
                    ReflectionUtils.invokeSetterMethod(r2, r2TargetName, r2Target);
                    finalR2s.add(r2);
                } catch (Exception e) {
                    throw new ServiceException("R2 class newInstance error", e);
                }
            });
        }
    }

    @Override
    @Transient
    @JsonProperty
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    public String getDisplay() {
        return "[" + getId() + "]" + this.getClass().getSimpleName();
    }
}
