package xyz.entdiy.shop.entity;

import com.entdiy.auth.entity.SiteUser;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "shop_ShopUser")
@MetaData(value = "微商城:客户信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopUser extends BaseNativeEntity {

    private static final long serialVersionUID = 2686339300612095738L;

    @MetaData(value = "下单用户")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "siteUser_id")
    private SiteUser siteUser;
}
