package lab.s2jh.module.sys.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.util.WebFormatter;
import lab.s2jh.core.web.json.ShortDateTimeJsonSerializer;
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
@Table(name = "sys_NotifyMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "公告消息")
public class NotifyMessage extends BaseNativeEntity {

    private static final long serialVersionUID = 2544390748513253055L;

    @MetaData(value = "标题")
    @Column(nullable = false)
    private String title;

    @MetaData(value = "生效标识", comments = "安排定时任务，基于publishTime和expireTime更新此值")
    @Column(nullable = false)
    private Boolean effective = Boolean.FALSE;

    @MetaData(value = "标识必须登录才能访问")
    @Column(nullable = false)
    private Boolean authRequired = Boolean.FALSE;

    @MetaData(value = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date publishTime;

    @MetaData(value = "到期时间", comments = "为空表示永不过期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date expireTime;

    @MetaData(value = "平台设置", comments = "没有值=全部，其他为如下几项逗号分隔组合：web-admin,web-site,ios,android,winphone, @see NotifyMessage#NotifyMessagePlatformEnum")
    @Column(length = 200, nullable = true)
    private String platform;

    @MetaData(value = "消息目标属性OR并集", comments = "用标签来进行大规模的设备属性、用户属性分群，各元素之间为OR取并集。没有值=全部，其他为数据字典项逗号分隔组合，如：student, teacher")
    @Column(length = 1000, nullable = true)
    private String audienceTags;

    @MetaData(value = "消息目标属性AND交集", comments = "用标签来进行大规模的设备属性、用户属性分群，各元素之间为AND取交集。没有值=全部，其他为数据字典项逗号分隔组合，如：student, school_01")
    @Column(length = 1000, nullable = true)
    private String audienceAndTags;

    @MetaData(value = "用户标识列表", comments = "User对象的alias列表，各元素之间为OR取并集。没有值=全部，其他为数据字典项逗号分隔组合，如：user_01,user_02")
    @Column(length = 1000, nullable = true)
    private String audienceAlias;

    @MetaData(value = "APP弹出提示内容", comments = "如果不为空则触发APP弹出通知，为空则不会弹出而只会推送应用消息")
    @Column(length = 200)
    private String notification;

    @MetaData(value = "最近推送时间", comments = "为空表示尚未推送过")
    private Date lastPushTime;

    @MetaData(value = "消息内容", comments = "可以是无格式的TEXT或格式化的HTMl，一般是在邮件或WEB页面查看的HTML格式详细内容")
    @Lob
    @Column(nullable = false)
    @JsonIgnore
    private String message;

    @MetaData(value = "总计查看用户数")
    private Integer readUserCount = 0;

    @MetaData(value = "排序号", tooltips = "数字越大显示越靠上")
    private Integer orderRank = 100;

    @MetaData(value = "关联附件个数", comments = "用于列表显示和关联处理附件清理判断")
    private Integer attachmentSize;

    @Transient
    @MetaData(value = "标识已读")
    private Boolean readed;

    public static enum NotifyMessagePlatformEnum {

        @MetaData(value = "后端系统")
        web_admin,

        @MetaData(value = "前端网站")
        web_site,

        @MetaData(value = "苹果iOS")
        ios,

        @MetaData(value = "安卓Android")
        android,

        @MetaData(value = "Win Phone")
        winphone;

    }

    @Override
    @Transient
    public String getDisplay() {
        return title;
    }

    @Transient
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

    @Transient
    public boolean isPublic() {
        if (StringUtils.isBlank(audienceTags) && StringUtils.isBlank(audienceAndTags) && StringUtils.isBlank(audienceAlias)) {
            return true;
        }
        return false;
    }

    @MetaData(value = "辅助方法：用于表单数据绑定")
    @Transient
    public String[] getPlatformSplit() {
        return StringUtils.isNotBlank(platform) ? platform.split(",") : null;
    }

    @MetaData(value = "辅助方法：用于表单数据绑定")
    @Transient
    public void setPlatformSplit(String[] platformSplit) {
        platform = platformSplit != null ? StringUtils.join(platformSplit, ",") : null;
    }
}
