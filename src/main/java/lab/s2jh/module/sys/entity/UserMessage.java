package lab.s2jh.module.sys.entity;

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

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.AttachmentableEntity;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.util.WebFormatter;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lab.s2jh.core.web.json.EntityIdDisplaySerializer;
import lab.s2jh.core.web.json.ShortDateTimeJsonSerializer;
import lab.s2jh.module.auth.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_UserMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "用户消息", comments = "如果用户消息量担心影响查询效率，可以考虑引入定期归档处理把过期消息搬迁归档")
public class UserMessage extends BaseNativeEntity implements AttachmentableEntity {

    private static final long serialVersionUID = 1685596718660284598L;

    @MetaData(value = "标题")
    @Column(nullable = false)
    private String title;

    @MetaData(value = "生效标识", comments = "安排定时任务，基于publishTime和expireTime更新此值")
    @Column(nullable = false)
    private Boolean effective = Boolean.FALSE;

    @MetaData(value = "发布时间")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date publishTime;

    @MetaData(value = "失效时间", comments = "预计的到期时间，过期后则消息不再提示用户")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date expireTime;

    @MetaData(value = "目标用户", comments = "此值为空则为公告消息")
    @ManyToOne
    @JoinColumn(name = "targetUser_id", nullable = true)
    @JsonSerialize(using = EntityIdDisplaySerializer.class)
    private User targetUser;

    @MetaData(value = "邮件推送消息")
    private Boolean emailPush = Boolean.FALSE;

    @MetaData(value = "短信推送消息")
    private Boolean smsPush = Boolean.FALSE;

    @MetaData(value = "外部链接")
    private String externalLink;

    @MetaData(value = "公告内容")
    @Lob
    @JsonIgnore
    private String htmlContent;

    @MetaData(value = "关联附件个数", comments = "用于列表显示和关联处理附件清理判断")
    private Integer attachmentSize;

    @MetaData(value = "首次阅读时间")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    private Date firstReadTime;

    @MetaData(value = "最后阅读时间")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
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
    public String getMessageAbstract() {
        if (StringUtils.isNotBlank(externalLink)) {
            return "外部链接：<a href='" + externalLink + "' target='_blank'>" + externalLink + "</a>";
        } else {
            //TODO 优化为提取HTML内容text摘要
            return StringUtils.substring(WebFormatter.html2text(htmlContent), 0, 50).trim() + "...";
        }
    }
}
