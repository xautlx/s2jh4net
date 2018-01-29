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
package com.entdiy.auth.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_User")
@MetaData(value = "管理端账号信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class User extends BaseNativeEntity {

    private static final long serialVersionUID = 8728775138491827366L;

    @MetaData(value = "登录账户对象")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MetaData(value = "所属部门")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @MetaData(value = "真实姓名")
    @Column(length = 128)
    private String trueName;

    @MetaData(value = "角色关联")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonIgnore
    private List<UserR2Role> userR2Roles;

    @MetaData(value = "已关联角色主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private Long[] selectedRoleIds;

    public Long[] getRoleIds() {
        if (CollectionUtils.isEmpty(userR2Roles)) {
            return null;
        }
        Long[] selectedRoleIds = new Long[userR2Roles.size()];
        for (int i = 0; i < selectedRoleIds.length; i++) {
            selectedRoleIds[i] = userR2Roles.get(i).getRole().getId();
        }
        return selectedRoleIds;
    }

    @Override
    @Transient
    public String getDisplay() {
        return this.getAccount().getDisplay();
    }
}
