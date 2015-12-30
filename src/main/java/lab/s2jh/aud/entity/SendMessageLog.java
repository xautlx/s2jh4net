package lab.s2jh.aud.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.util.ExtStringUtils;
import lab.s2jh.core.util.WebFormatter;
import lab.s2jh.core.web.json.JsonViews;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_SendMessageLog")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@MetaData(value = "发送消息记录", comments = "包括电子邮件，短信，推送等消息流水记录")
public class SendMessageLog extends PersistableEntity<Long> {

    private static final long serialVersionUID = -541805294603254373L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "native")
    private Long id;

    @MetaData(value = "消息接受者")
    @Column(length = 300, nullable = false)
    private String targets;

    @MetaData(value = "标题")
    @Column(length = 256, nullable = true)
    private String title;

    @MetaData(value = "消息内容", comments = "可以是无格式的TEXT或格式化的HTMl")
    @Lob
    @Column(nullable = false)
    @JsonIgnore
    private String message;

    @MetaData(value = "消息响应", comments = "如JSON，HTML响应文本")
    @Lob
    @Column(nullable = true)
    private String response;

    @MetaData(value = "消息类型")
    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private SendMessageTypeEnum messageType;

    @MetaData(value = "发送时间")
    @Column(nullable = false, updatable = false)
    private Date sendTime;

    public static enum SendMessageTypeEnum {
        @MetaData(value = "API")
        API,

        @MetaData(value = "电子邮件")
        EMAIL,

        @MetaData(value = "手机短信")
        SMS,

        @MetaData(value = "APP推送通知")
        APP_PUSH;
    }

    @Override
    @Transient
    public String getDisplay() {
        return title;
    }

    @Transient
    @JsonView(JsonViews.Admin.class)
    public String getMessageAbstract() {
        if (!StringUtils.isEmpty(message)) {
            String text = WebFormatter.html2text(message);
            return ExtStringUtils.cutRedundanceStr(text, 200);
        } else {
            return "";
        }
    }
}
