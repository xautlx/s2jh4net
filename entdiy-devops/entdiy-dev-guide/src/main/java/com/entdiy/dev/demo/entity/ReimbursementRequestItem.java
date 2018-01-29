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
package com.entdiy.dev.demo.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "demo_ReimbursementRequestItem")
@MetaData(value = "报销申请")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class ReimbursementRequestItem extends BaseNativeEntity {

    @MetaData(value = "报销申请主对象", validate = false)
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "reimbursementRequest_id", nullable = false)
    private ReimbursementRequest reimbursementRequest;

    @MetaData(value = "费用发生起始日期")
    @Column(nullable = false)
    private LocalDate startDate;

    @MetaData(value = "费用发生结束日期", tooltips = "留空表示与起始日期相同，单日费用")
    @Column(nullable = true)
    private LocalDate endDate;

    @MetaData(value = "报销类目")
    @Column(nullable = false, length = 128)
    private String useType;

    @MetaData(value = "公务描述")
    @Column(length = 2000)
    private String useExplain;

    @MetaData(value = "票据张数")
    @Column(nullable = false)
    private Integer invoiceCount = 1;

    @MetaData(value = "票据小计金额")
    @Column(nullable = false)
    private BigDecimal invoiceAmount;
}
