package lab.s2jh.module.auth.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_UserR2Role", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "role_id" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "登录账号与角色关联")
public class UserR2Role extends BaseNativeEntity {

    private static final long serialVersionUID = -1727859177925448384L;

    @MetaData(value = "登录账号对象")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MetaData(value = "关联角色对象")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Transient
    @Override
    public String getDisplay() {
        return user.getDisplay() + "_" + role.getDisplay();
    }
}
