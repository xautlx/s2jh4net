/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.dev.demo.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    @MetaData(value = "报销申请主对象")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "reimbursementRequest_id", nullable = false)
    private ReimbursementRequest reimbursementRequest;

    @MetaData(value = "费用发生起始日期")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_FORMAT)
    @JsonFormat(pattern = DateUtils.DEFAULT_DATE_FORMAT)
    @Column(nullable = false)
    private Date startDate;

    @MetaData(value = "费用发生结束日期", tooltips = "默认等于开始日期，表示单日事件")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_FORMAT)
    @JsonFormat(pattern = DateUtils.DEFAULT_DATE_FORMAT)
    @Column(nullable = true)
    private Date endDate;

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
