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
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Role")
@MetaData(value = "角色")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class Role extends BaseNativeEntity {

    private static final long serialVersionUID = 7955799161213060384L;

    @MetaData(value = "代码", tooltips = "必须以ROLE_打头")
    @Size(min = 6)
    @Pattern(regexp = "^ROLE_.*", message = "必须以[ROLE_]开头")
    @Column(nullable = false, length = 64, unique = true)
    private String code;

    @MetaData(value = "名称")
    @Column(nullable = false, length = 256)
    private String name;

    @MetaData(value = "描述")
    @Column(nullable = true, length = 2000)
    private String description;

    @MetaData(value = "禁用标识", tooltips = "禁用角色不参与权限控制逻辑")
    private Boolean disabled = Boolean.FALSE;

    @MetaData(value = "角色权限关联")
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonIgnore
    private List<RoleR2Privilege> roleR2Privileges = Lists.newArrayList();

    @MetaData(value = "获取已关联权限主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    public List<Long> getrivilegeIds() {
        if (CollectionUtils.isEmpty(roleR2Privileges)) {
            return null;
        }
        return roleR2Privileges.stream().map(r2 -> r2.getPrivilege().getId()).collect(Collectors.toList());
    }

    @MetaData(value = "设置已关联权限主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    public void setPrivilegeIds(List<Long> privilegeIds) {
        //没有关联对象集合，清空关联集合
        if (CollectionUtils.isEmpty(privilegeIds) && CollectionUtils.isNotEmpty(roleR2Privileges)) {
            roleR2Privileges.clear();
        } else {
            //有关联对象集合，如果原来没有则初始化创建，否则移除不存在关联
            if (CollectionUtils.isEmpty(roleR2Privileges)) {
                roleR2Privileges = Lists.newArrayList();
            } else {
                roleR2Privileges.removeIf(r2 -> !privilegeIds.stream().anyMatch(privilegeId -> privilegeId.equals(r2.getPrivilege().getId())));
            }
            //先移除已经存在的关键对象主键，然后追加剩余新增关联
            privilegeIds.removeIf(privilegeId -> roleR2Privileges.stream().anyMatch(r2 -> r2.getPrivilege().getId().equals(privilegeId)));
            privilegeIds.stream().forEach(privilegeId -> {
                RoleR2Privilege r2 = new RoleR2Privilege();
                r2.setRole(this);
                Privilege privilege = new Privilege();
                privilege.setId(privilegeId);
                r2.setPrivilege(privilege);
                roleR2Privileges.add(r2);
            });
        }
    }

    @MetaData(value = "角色关联用户")
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonIgnore
    private List<UserR2Role> roleR2Users = Lists.newArrayList();

    @Override
    @Transient
    public String getDisplay() {
        return code + " " + name;
    }
}
