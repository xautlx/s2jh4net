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
package com.entdiy.sys.entity;

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
@Table(name = "sys_SmsVerifyCode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "短信校验码")
public class SmsVerifyCode extends BaseNativeEntity {

    private static final long serialVersionUID = 615208416034164816L;

    @Column(length = 32, nullable = false, unique = true)
    private String mobileNum;

    @Column(length = 32, nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime generateTime;

    @MetaData(value = "过期时间", comments = "定时任务定期清理过期的校验码")
    @Column(nullable = false)
    private LocalDateTime expireTime;

    @MetaData(value = "首次验证通过时间", comments = "验证通过的手机号保留下来")
    @Column(nullable = true)
    private LocalDateTime firstVerifiedTime;

    @MetaData(value = "最后验证通过时间", comments = "验证通过的手机号保留下来")
    @Column(nullable = true)
    private LocalDateTime lastVerifiedTime;

    @MetaData(value = "总计验证通过次数")
    @Column(nullable = true)
    private Integer totalVerifiedCount = 0;
}
