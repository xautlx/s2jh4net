package lab.s2jh.module.schedule.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sche_JobBeanCfg")
@MetaData(value = "定时任务配置")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class JobBeanCfg extends BaseNativeEntity {

    private static final long serialVersionUID = -416068377592076851L;

    @MetaData(value = "任务类全名", tooltips = "实现QuartzJobBean的类全路径类名 ")
    @Column(length = 128, nullable = false, unique = true)
    private String jobClass;

    @MetaData(value = "CRON表达式", tooltips = "Cron表达式的格式：秒 分 时 日 月 周 年(可选)")
    @Column(length = 64, nullable = false)
    private String cronExpression;

    @MetaData(value = "自动初始运行")
    @Column(nullable = false)
    private Boolean autoStartup = Boolean.TRUE;

    @MetaData(value = "启用运行记录", tooltips = "每次运行会写入历史记录表，对于运行频率很高或业务监控意义不大的任务建议关闭")
    @Column(nullable = false)
    private Boolean logRunHist = Boolean.TRUE;

    @MetaData(value = "集群运行模式", tooltips = "如果为true，则在一组集群部署环境中同一任务只会在一个节点触发<br/>否则在每个节点各自独立运行")
    private Boolean runWithinCluster = Boolean.TRUE;

    @MetaData(value = "描述")
    @Column(length = 1000, nullable = true)
    private String description;

    @MetaData(value = "结果模板文本")
    @Lob
    private String resultTemplate;
}
