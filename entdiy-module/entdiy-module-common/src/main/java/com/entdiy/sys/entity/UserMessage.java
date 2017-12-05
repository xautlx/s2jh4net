package com.entdiy.sys.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.entdiy.auth.entity.User;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.util.WebFormatter;
import com.entdiy.core.web.json.DateTimeJsonSerializer;
import com.entdiy.core.web.json.EntityIdDisplaySerializer;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.json.ShortDateTimeJsonSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_UserMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "用户消息", comments = "如果用户消息量担心影响查询效率，可以考虑引入定期归档处理把过期消息搬迁归档")
public class UserMessage extends BaseNativeEntity {

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

    @MetaData(value = "目标用户")
    @ManyToOne
    @JoinColumn(name = "targetUser_id", nullable = false)
    @JsonSerialize(using = EntityIdDisplaySerializer.class)
    @JsonView(JsonViews.Admin.class)
    private User targetUser;

    @MetaData(value = "发布时间", comments = "全局的消息创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date publishTime;

    @MetaData(value = "邮件推送消息")
    @JsonView(JsonViews.Admin.class)
    private Boolean emailPush = Boolean.FALSE;

    @MetaData(value = "邮件推送消息时间", comments = "为空表示尚未推送过")
    @JsonView(JsonViews.Admin.class)
    private Date emailPushTime;

    @MetaData(value = "短信推送消息")
    @JsonView(JsonViews.Admin.class)
    private Boolean smsPush = Boolean.FALSE;

    @MetaData(value = "短信推送消息时间", comments = "为空表示尚未推送过")
    @JsonView(JsonViews.Admin.class)
    private Date smsPushTime;

    @MetaData(value = "APP推送消息")
    @JsonView(JsonViews.Admin.class)
    private Boolean appPush = Boolean.FALSE;

    @MetaData(value = "APP推送消息时间", comments = "为空表示尚未推送过")
    @JsonView(JsonViews.Admin.class)
    private Date appPushTime;

    @MetaData(value = "关联附件个数", comments = "用于列表显示和关联处理附件清理判断")
    @JsonView(JsonViews.Admin.class)
    private Integer attachmentSize;

    @MetaData(value = "首次阅读时间")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonView(JsonViews.Admin.class)
    private Date firstReadTime;

    @MetaData(value = "最后阅读时间")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonView(JsonViews.Admin.class)
    private Date lastReadTime;

    @MetaData(value = "总计阅读次数")
    @Column(nullable = false)
    private Integer readTotalCount = 0;

    @Override
    @Transient
    public String getDisplay() {
        return title;
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
