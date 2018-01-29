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
package com.entdiy.auth.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.entity.EnumKeyLabelPair;
import com.entdiy.core.web.json.LocalDateSerializer;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Account", uniqueConstraints = @UniqueConstraint(columnNames = {"authType", "authUid", "_dataDomain"}))
@MetaData(value = "登录信息对象")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Account extends BaseNativeEntity {

    private static final long serialVersionUID = 8977448800400578128L;

    @MetaData(value = "账户类型")
    @Column(length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthTypeEnum authType;

    @MetaData(value = "账号类型所对应唯一标识")
    @Column(length = 64, nullable = false)
    private String authUid;

    @MetaData(value = "注册时间")
    @Column(updatable = false)
    private LocalDateTime signupTime;

    @MetaData(value = "密码加密盐值")
    @Column(length = 128, nullable = false)
    private String salt;

    @MetaData(value = "用户密码", comments = "加密算法：MD5({salt}+原始密码)")
    @JsonIgnore
    private String password;

    @MetaData(value = "电子邮件", tooltips = "请仔细填写，可用于系统通知邮件发送，找回密码等功能")
    @Email
    @Column(unique = true, nullable = true)
    private String email;

    @MetaData(value = "移动电话", tooltips = "请仔细填写，可用于系统通知短信发送，找回密码等功能")
    @Column(unique = true, nullable = true)
    @Size(min = 11, max = 18)
    private String mobile;

    @MetaData(value = "账户未锁定标志", tooltips = "账号锁定后无法登录")
    private Boolean accountNonLocked = Boolean.TRUE;

    @MetaData(value = "失效日期", tooltips = "设定账号访问系统的失效日期，为空表示永不失效")
    private LocalDate accountExpireDate;

    @MetaData(value = "账户密码过期时间", tooltips = "到期后强制用户登录成功后必须修改密码", comments = "比如用于初始化密码时设置当前时间，这样用户下次登录成功后则强制用户必须修改密码。")
    private LocalDate credentialsExpireDate;

    @MetaData(value = "最近认证失败次数", comments = "认证失败累加，成功后清零。达到设定失败次数后锁定帐号，防止无限制次数尝试猜测密码")
    private Integer lastFailureTimes = 0;

    @MetaData(value = "总计认证成功次数", comments = "不断累加")
    private Long logonSuccessTimes = 0L;

    @MetaData(value = "总计认证失败次数", comments = "不断累加")
    private Long logonFailureTimes = 0L;

    @MetaData(value = "最近认证失败时间")
    private LocalDateTime lastLogonFailureTime;

    @MetaData(value = "最近认证成功时间")
    private LocalDateTime lastLogonSuccessTime;

    @MetaData(value = "REST访问Token")
    @Column(unique = true)
    private String accessToken;

    @MetaData(value = "随机数", comments = "用于找回密码设定的随机UUID字符串")
    private String randomCode;

    public enum AuthTypeEnum implements EnumKeyLabelPair {
        admin {
            @Override
            public String getLabel() {
                return "管理用户";
            }
        },

        site {
            @Override
            public String getLabel() {
                return "前端用户";
            }
        }
    }

    @Transient
    @JsonIgnore
    public String getSignupTimeFormatted() {
        return signupTime == null ? "" : signupTime.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER);
    }

    @Transient
    @JsonIgnore
    public String getLastLogonSuccessTimeFormatted() {
        return lastLogonSuccessTime == null ? "" : lastLogonSuccessTime.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER);
    }

    @Transient
    @JsonIgnore
    public String getAccountExpireDateFormatted() {
        return accountExpireDate == null ? "" : accountExpireDate.format(LocalDateSerializer.LOCAL_DATE_FORMATTER);
    }

    @Override
    @Transient
    public String getDisplay() {
        String dataDomain = getDataDomain();
        if (dataDomain == null || GlobalConstant.DEFAULT_VALUE.equals(dataDomain) || GlobalConstant.ROOT_VALUE.equals(dataDomain)) {
            return authUid;
        } else {
            return dataDomain + "/" + authUid;
        }
    }

    @MetaData(value = "用户标识别名", comments = "用户在多个终端登录，需要一个标识同一个身份以便多终端推送消息")
    @Transient
    public String getAlias() {
        return getDataDomain() + "/" + authUid;
    }
}
