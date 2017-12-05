package com.entdiy.auth.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Privilege")
@MetaData(value = "权限")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Privilege extends BaseNativeEntity {

    private static final long serialVersionUID = 5139319086984812835L;

    @MetaData(value = "代码")
    @Column(nullable = false, length = 255, unique = true)
    private String code;

    @MetaData(value = "禁用标识", tooltips = "禁用不参与权限控制逻辑")
    private Boolean disabled = Boolean.FALSE;

    @MetaData(value = "重建时间")
    private Date rebuildTime;

    @MetaData(value = "角色权限关联")
    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RoleR2Privilege> roleR2Privileges = Lists.newArrayList();

}