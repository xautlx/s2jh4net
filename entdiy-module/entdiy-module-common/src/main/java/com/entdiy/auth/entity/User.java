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
import com.entdiy.core.entity.BaseEntity;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_User")
@MetaData(value = "管理端账号信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class User extends BaseEntity<Long> {

    @MetaData("主键")
    @Id
    @JsonProperty
    @JsonView(JsonViews.Public.class)
    private Long id;

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


    @MetaData(value = "获取已关联角色主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    public List<Long> getRoleIds() {
        if (CollectionUtils.isEmpty(userR2Roles)) {
            return null;
        }
        return userR2Roles.stream().map(r2 -> r2.getRole().getId()).collect(Collectors.toList());
    }

    @MetaData(value = "设置已关联角色主键集合", comments = "辅助属性：用于页面表单标签进行数据绑定")
    @Transient
    @JsonIgnore
    public void setRoleIds(List<Long> roleIds) {
        //没有关联对象集合，清空关联集合
        if (CollectionUtils.isEmpty(roleIds) && CollectionUtils.isNotEmpty(userR2Roles)) {
            userR2Roles.clear();
        } else {
            //有关联对象集合，如果原来没有则初始化创建，否则移除不存在关联
            if (CollectionUtils.isEmpty(userR2Roles)) {
                userR2Roles = Lists.newArrayList();
            } else {
                userR2Roles.removeIf(r2 -> !roleIds.stream().anyMatch(roleId -> roleId.equals(r2.getRole().getId())));
            }
            //先移除已经存在的关键对象主键，然后追加剩余新增关联
            roleIds.removeIf(roleId -> userR2Roles.stream().anyMatch(r2 -> r2.getRole().getId().equals(roleId)));
            roleIds.forEach(roleId -> {
                UserR2Role r2 = new UserR2Role();
                r2.setUser(this);
                Role role = new Role();
                role.setId(roleId);
                r2.setRole(role);
                userR2Roles.add(r2);
            });
        }
    }

    @Override
    @Transient
    public String getDisplay() {
        return this.getAccount().getDisplay();
    }
}
