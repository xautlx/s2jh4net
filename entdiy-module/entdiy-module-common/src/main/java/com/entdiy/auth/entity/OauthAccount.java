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
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_OauthAccount", uniqueConstraints = {@UniqueConstraint(columnNames = {"oauthType", "oauthOpenId", "authType"})})
@MetaData(value = "OAuth认证对象")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class OauthAccount extends BaseNativeEntity {

    @MetaData(value = "绑定关联账户对象")
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "account_id", nullable = true)
    @JsonIgnore
    private Account account;

    @MetaData(value = "OAuth类型")
    @Column(length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private GlobalConstant.OauthTypeEnum oauthType;

    @MetaData(value = "账号类型所对应唯一标识")
    @Column(length = 64, nullable = false)
    private String oauthOpenId;

    @MetaData(value = "内部账户类型")
    @Column(length = 64, nullable = true)
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.Admin.class)
    private Account.AuthTypeEnum authType;

    @MetaData(value = "绑定时间")
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime bindTime;

    @JsonView(JsonViews.Admin.class)
    @Embedded
    private OauthAccessToken oauthAccessToken;

    @JsonView(JsonViews.App.class)
    @Embedded
    private OauthUserinfo oauthUserinfo;

    @JsonView(JsonViews.Admin.class)
    public String getNickname() {
        if (oauthUserinfo != null) {
            return oauthUserinfo.getNickname();
        }
        return GlobalConstant.NONE_VALUE;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    @Setter
    @Embeddable
    public static class OauthAccessToken {

        @JsonView(JsonViews.App.class)
        private String accessToken;

        @JsonView(JsonViews.Admin.class)
        private int expiresIn = -1;

        @JsonIgnore
        private String refreshToken;

        @JsonView(JsonViews.Admin.class)
        private String scope;

        /**
         * https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&announce_id=11513156443eZYea&version=&lang=zh_CN.
         * 本接口在scope参数为snsapi_base时不再提供unionID字段。
         * <pre>
         * 只有在将公众号绑定到微信开放平台帐号后，才会出现该字段。
         * 另外，在用户未关注公众号时，将不返回用户unionID信息。
         * 已关注的用户，开发者可使用“获取用户基本信息接口”获取unionID；
         * 未关注用户，开发者可使用“微信授权登录接口”并将scope参数设置为snsapi_userinfo，获取用户unionID
         * </pre>
         */
        @JsonView(JsonViews.Admin.class)
        private String unionId;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    @Setter
    @Embeddable
    public static class OauthUserinfo {

        /**
         * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息，只有openid和UnionID（在该公众号绑定到了微信开放平台账号时才有）
         */
        @JsonView(JsonViews.Admin.class)
        private Boolean subscribe;

        @JsonView(JsonViews.Admin.class)
        private String openId;

        @JsonView(JsonViews.App.class)
        private String nickname;

        /**
         * 性别描述信息：男、女、未知等.
         */
        @JsonView(JsonViews.App.class)
        private String sexDesc;

        /**
         * 性别表示：1，2等数字.
         */
        @JsonView(JsonViews.Admin.class)
        private Integer sex;

        /**
         * 用户的语言，简体中文为zh_CN
         */
        @JsonView(JsonViews.Admin.class)
        private String language;

        @JsonView(JsonViews.Admin.class)
        private String city;

        @JsonView(JsonViews.Admin.class)
        private String province;

        @JsonView(JsonViews.Admin.class)
        private String country;

        @Column(length = 1024)
        @JsonView(JsonViews.App.class)
        private String headImgUrl;

        /**
         * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
         */
        @JsonView(JsonViews.Admin.class)
        private Long subscribeTime;

        @JsonView(JsonViews.Admin.class)
        private String remark;

        @JsonView(JsonViews.Admin.class)
        private Integer groupId;

        /** 逗号分隔 */
        @JsonView(JsonViews.Admin.class)
        private String tagIds;

        /**
         * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）.
         */
        @JsonView(JsonViews.Admin.class)
        private String privileges;
    }

    @Override
    @Transient
    @ApiModelProperty(hidden = true)
    public String getDisplay() {
        return oauthType + "/" + oauthOpenId;
    }
}
