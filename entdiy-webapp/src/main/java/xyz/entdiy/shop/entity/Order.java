package xyz.entdiy.shop.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "shopUser_id")
    private ShopUser shopUser;

    @MetaData(value = "订单号")
    @Column(length = 20, nullable = false, unique = true)
    private String orderNo;

    private LocalDateTime submitTime;

    private LocalDateTime payTime;

    @Override
    @Transient
    public String getDisplay() {
        return orderNo;
    }
}
