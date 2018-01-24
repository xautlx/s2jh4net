package com.entdiy.auth.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_SiteUser")
@MetaData(value = "前端用户账号信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class SiteUser extends BaseNativeEntity {

    private static final long serialVersionUID = 2686339300612095738L;

    @MetaData(value = "登录账户对象")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MetaData(value = "头像")
    @Column(length = 512)
    private String headPhoto;

    @Override
    @Transient
    public String getDisplay() {
        return this.getAccount().getDisplay();
    }
}
