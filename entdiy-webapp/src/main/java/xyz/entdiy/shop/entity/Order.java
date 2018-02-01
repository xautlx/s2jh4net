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
    @JoinColumn(name = "siteUser_id")
    private SiteUser siteUser;

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
