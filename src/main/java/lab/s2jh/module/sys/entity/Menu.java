package lab.s2jh.module.sys.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_Menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "菜单")
public class Menu extends BaseNativeEntity {

    private static final long serialVersionUID = 2860233299443173932L;

    /** 用于报表菜单项计算的固定菜单代码 */
    public static final String MENU_CODE_RPT = "MFIXRPT";

    @MetaData(value = "名称")
    @Column(nullable = false, length = 32)
    private String name;

    @MetaData(value = "菜单路径")
    @Column(nullable = false, length = 255, unique = true)
    private String path;

    @MetaData(value = "父节点")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "none"))
    @JsonIgnore
    private Menu parent;

    @MetaData(value = "描述")
    @Column(length = 1000)
    @JsonIgnore
    private String description;

    @MetaData(value = "禁用标识", tooltips = "禁用菜单全局不显示")
    private Boolean disabled = Boolean.FALSE;

    @MetaData(value = "菜单URL")
    @Column(length = 256)
    private String url;

    @MetaData(value = "图标样式")
    @Column(length = 128)
    private String style;

    @MetaData(value = "排序号", tooltips = "相对排序号，数字越大越靠上显示")
    @Column(nullable = false)
    private Integer orderRank = 100;

    @MetaData(value = "展开标识", tooltips = "是否默认展开菜单组")
    private Boolean initOpen = Boolean.FALSE;

    @MetaData(value = "所在层级")
    private Integer inheritLevel;

    @MetaData(value = "对应Web Controller类名")
    @Column(length = 256)
    private String controllerClass;

    @MetaData(value = "对应Web Controller调用方法名")
    @Column(length = 128)
    private String controllerMethod;

    @MetaData(value = "重建时间")
    private Date rebuildTime;

    @Override
    @Transient
    public String getDisplay() {
        return path;
    }
}
