package xyz.entdiy.shop.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.entdiy.auth.entity.User;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
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
@Table(name = "shop_SiteUser")
@MetaData(value = "前端用户信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SiteUser extends BaseNativeEntity {

    private static final long serialVersionUID = 2686339300612095738L;

    @MetaData(value = "登录账号对象")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @MetaData(value = "头像")
    @Column(length = 512)
    private String headPhoto;

    @Override
    @Transient
    public String getDisplay() {
        return user.getDisplay();
    }
}
