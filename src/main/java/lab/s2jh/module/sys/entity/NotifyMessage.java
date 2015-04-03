package lab.s2jh.module.sys.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.AttachmentableEntity;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_NotifyMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "公告消息")
public class NotifyMessage extends BaseNativeEntity implements AttachmentableEntity {

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

    @MetaData(value = "到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = ShortDateTimeJsonSerializer.class)
    private Date expireTime;

    @MetaData(value = "前端网站显示")
    public static final Integer SHOW_SCOPE_SITE = 1;
    @MetaData(value = "管理系统显示")
    public static final Integer SHOW_SCOPE_ADMIN = 2;
    @MetaData(value = "APP客户端显示")
    public static final Integer SHOW_SCOPE_APP = 4;

    public static final Map<Integer, String> showScopeMap = Maps.newHashMap();
    static {
        showScopeMap.put(SHOW_SCOPE_SITE, "前端网站显示");
        showScopeMap.put(SHOW_SCOPE_ADMIN, "管理系统显示");
        showScopeMap.put(SHOW_SCOPE_APP, "APP客户端显示");
    }

    /**
     * 基于二进制与非操作设定
     * @see #showScopeMap
     */
    @MetaData(value = "公告显示的范围代码")
    private Integer showScopeCode = 0;

    @MetaData(value = "外部链接")
    private String externalLink;

    @MetaData(value = "公告内容")
    @Lob
    @JsonIgnore
    private String htmlContent;

    @MetaData(value = "总计查看用户数")
    private Integer readUserCount = 0;

    @MetaData(value = "排序号", tooltips = "数字越大显示越靠上")
    private Integer orderRank = 100;

    @MetaData(value = "关联附件个数", comments = "用于列表显示和关联处理附件清理判断")
    private Integer attachmentSize;

    @Transient
    @MetaData(value = "标识已读")
    private Boolean readed;

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

    @Transient
    public String getMessageType() {
        List<String> showScopes = Lists.newArrayList();
        for (Map.Entry<Integer, String> me : showScopeMap.entrySet()) {
            if ((showScopeCode & me.getKey()) == me.getKey()) {
                showScopes.add(me.getValue());
            }
        }
        if (showScopes.size() == 0) {
            return "待定";
        } else {
            return StringUtils.join(showScopes, ",");
        }
    }

    @Transient
    public void setShowScopeAll() {
        int code = 0;
        for (Integer key : showScopeMap.keySet()) {
            code += key;
        }
        this.showScopeCode = code;
    }
}
