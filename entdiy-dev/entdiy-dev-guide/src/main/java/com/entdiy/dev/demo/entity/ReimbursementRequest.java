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

import com.entdiy.auth.entity.Department;
import com.entdiy.auth.entity.User;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "demo_ReimbursementRequest")
@MetaData(value = "报销申请")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class ReimbursementRequest extends BaseNativeEntity {
    @MetaData(value = "登录账号对象")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MetaData(value = "挂账部门", comments = "默认取用户所属部门，填写时可按实际挂账部门选择，影响后续审批部门相关流程节点")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @MetaData(value = "提交时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_TIME_FORMAT)
    @JsonFormat(pattern = DateUtils.DEFAULT_TIME_FORMAT)
    @Column(nullable = false)
    private Date submitTime = new Date();

    @MetaData(value = "报销摘要说明")
    @Column(length = 2000)
    private String abstractExplain;

    @MetaData(value = "关联行项")
    @OneToMany(mappedBy = "reimbursementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonIgnore
    private List<ReimbursementRequestItem> reimbursementRequestItems;
}
