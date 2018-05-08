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
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.entity.EnumKeyLabelPair;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_OauthAccount", uniqueConstraints = @UniqueConstraint(columnNames = {"oauthType", "oauthOpenId"}))
@MetaData(value = "OAuth认证对象")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class OauthAccount extends BaseNativeEntity {

    @MetaData(value = "绑定关联账户对象")
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MetaData(value = "账户类型")
    @Column(length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private OauthTypeEnum oauthType;

    @MetaData(value = "账号类型所对应唯一标识")
    @Column(length = 64, nullable = false)
    private String oauthOpenId;

    public enum OauthTypeEnum implements EnumKeyLabelPair {
        weixin {
            @Override
            public String getLabel() {
                return "微信";
            }
        },

        alipay {
            @Override
            public String getLabel() {
                return "支付宝";
            }
        }
    }

    @Override
    @Transient
    @ApiModelProperty(hidden = true)
    public String getDisplay() {
        return oauthType + "/" + oauthOpenId;
    }
}
