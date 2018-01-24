/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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
package com.entdiy.aud.entity;

import com.entdiy.auth.entity.Account;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.AbstractPersistableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_AccountLogonLog")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@MetaData(value = "账户登录登出历史记录")
public class AccountLogonLog extends AbstractPersistableEntity<Long> {

    private static final long serialVersionUID = 4034691676061136485L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "native")
    private Long id;

    @MetaData(value = "登录账户对象")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MetaData(value = "便于按日汇总统计的冗余属性")
    @Column(nullable = true)
    private LocalDate logonDate;

    @MetaData(value = "登录时间")
    @Column(nullable = false)
    private LocalDateTime logonTime;

    @MetaData(value = "登出时间")
    private LocalDateTime logoutTime;

    @MetaData(value = "登录时长")
    private Long logonTimeLength;

    @MetaData(value = "总计登录次数")
    private Long logonTimes;

    @MetaData(value = "userAgent")
    private String userAgent;

    @MetaData(value = "xforwardFor")
    private String xforwardFor;

    @MetaData(value = "localAddr")
    private String localAddr;

    @MetaData(value = "localName")
    private String localName;

    @MetaData(value = "localPort")
    private Integer localPort;

    @MetaData(value = "remoteAddr")
    private String remoteAddr;

    @MetaData(value = "remoteHost")
    private String remoteHost;

    @MetaData(value = "remotePort")
    private Integer remotePort;

    @MetaData(value = "serverIP")
    private String serverIP;

    @MetaData(value = "Session编号")
    @Column(length = 128, nullable = false, unique = true)
    private String httpSessionId;

    @Override
    @Transient
    public String getDisplay() {
        return account.getDisplay();
    }

    @Transient
    public String getLogonTimeLengthFriendly() {
        //TODO 优化为格式化显示
        return logonTimeLength == null ? "" : logonTimeLength + " s";
    }
}