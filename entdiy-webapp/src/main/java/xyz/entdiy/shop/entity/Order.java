package xyz.entdiy.shop.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "shop_Order")
@MetaData(value = "订单")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order extends BaseNativeEntity {

    private static final long serialVersionUID = 3583296283380036832L;

    @MetaData(value = "下单用户")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "siteUser_id")
    private SiteUser siteUser;

    @MetaData(value = "订单号")
    @Column(length = 20, nullable = false, unique = true)
    private String orderNo;

    private Date submitTime;

    private Date payTime;

    @Override
    @Transient
    public String getDisplay() {
        return orderNo;
    }
}
