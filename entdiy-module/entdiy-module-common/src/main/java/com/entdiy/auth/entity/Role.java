package com.entdiy.auth.entity;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

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
