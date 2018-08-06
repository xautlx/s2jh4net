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
package com.entdiy.sys.entity;

import com.entdiy.auth.entity.Account;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.util.WebFormatter;
import com.entdiy.core.web.json.EntityIdDisplaySerializer;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_AccountMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "账号消息", comments = "如果用户消息量担心影响查询效率，可以考虑引入定期归档处理把过期消息搬迁归档")
public class AccountMessage extends BaseNativeEntity {

    private static final long serialVersionUID = 1685596718660284598L;

    @MetaData(value = "消息类型", comments = "从数据字典定义的消息类型")
    @Column(length = 32, nullable = true)
    private String type;

    @MetaData(value = "标题")
    @Column(nullable = false)
    private String title;

    @MetaData(value = "APP弹出提示内容", comments = "如果不为空则触发APP弹出通知，为空则不会弹出而只会推送应用消息")
    @Column(length = 200)
    @JsonView(JsonViews.Admin.class)
    private String notification;

    @MetaData(value = "消息内容", comments = "可以是无格式的TEXT或格式化的HTMl，一般是在邮件或WEB页面查看的HTML格式详细内容")
    @Lob
    @Column(nullable = false)
    @JsonView(JsonViews.AppDetail.class)
    private String message;

    @MetaData(value = "目标账号")
    @ManyToOne
    @JoinColumn(name = "targetAccount_id", nullable = false)
    @JsonSerialize(using = EntityIdDisplaySerializer.class)
    @JsonView(JsonViews.Admin.class)
    private Account targetAccount;

    @MetaData(value = "发布时间", comments = "全局的消息创建时间")
    @Column(nullable = false)
    private LocalDateTime publishTime;

    @MetaData(value = "邮件推送消息")
    @JsonView(JsonViews.Admin.class)
    private Boolean emailPush = Boolean.FALSE;

    @MetaData(value = "邮件推送消息时间", comments = "为空表示尚未推送过")
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime emailPushTime;

    @MetaData(value = "APP推送消息")
    @JsonView(JsonViews.Admin.class)
    private Boolean appPush = Boolean.FALSE;

    @MetaData(value = "APP推送消息时间", comments = "为空表示尚未推送过")
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime appPushTime;

    @MetaData(value = "关联附件个数", comments = "用于列表显示和关联处理附件清理判断")
    @JsonView(JsonViews.Admin.class)
    private Integer attachmentSize;

    @MetaData(value = "首次阅读时间")
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime firstReadTime;

    @MetaData(value = "最后阅读时间")
    @JsonView(JsonViews.Admin.class)
    private LocalDateTime lastReadTime;

    @MetaData(value = "总计阅读次数")
    @Column(nullable = false)
    private Integer readTotalCount = 0;

    @Override
    @Transient
    public String getDisplay() {
        return title;
    }

    @Transient
    @JsonIgnore
    public String getPublishTimeFormatted() {
        return publishTime == null ? "" : publishTime.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER);
    }

    @Transient
    @JsonView(JsonViews.Admin.class)
    public String getMessageAbstract() {
        if (StringUtils.isNotBlank(notification)) {
            return notification;
        } else {
            //优化为提取HTML内容text摘要
            if (!StringUtils.isEmpty(message)) {
                return StringUtils.substring(WebFormatter.html2text(message), 0, 50).trim() + "...";
            } else {
                return "";
            }
        }
    }
}
