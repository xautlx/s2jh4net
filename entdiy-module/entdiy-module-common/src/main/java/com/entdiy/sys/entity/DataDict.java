package com.entdiy.sys.entity;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.web.json.EntityIdDisplaySerializer;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.sys.service.DataDictService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_DataDict", uniqueConstraints = @UniqueConstraint(columnNames = { "parent_id", "primaryKey", "secondaryKey" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "数据字典")
@Audited
public class DataDict extends BaseNativeEntity {

    private static final long serialVersionUID = 5732022663570063926L;

    /** 
     * 字典数据的主标识，绝大部分情况对于单一主标识就能确定唯一性的字典数据只需维护此字段值即可
     * 注意：primaryKey+secondaryKey+parent唯一性约束
     */
    @MetaData(value = "主标识")
    @Column(length = 128, nullable = false)
    @JsonView(JsonViews.List.class)
    private String primaryKey;

    /** 
     * 字典数据的secondaryKey值，如果primaryKey值不能单一确定唯一性则可以启用secondaryKey值进行组合唯一控制
     */
    @MetaData(value = "次标识")
    @Column(length = 128)
    @JsonView(JsonViews.List.class)
    private String secondaryKey;

    /**
     * 字典数据对应的数据Value值
     * 大部分情况一般都是key-value形式的数据，只需要维护primaryKey和primaryValue即可，
     * 然后通过{@link DataDictService#findChildrenByPrimaryKey(String)}即可快速返回key-value形式的Map数据
     */
    @MetaData(value = "主要数据")
    @JsonView(JsonViews.List.class)
    private String primaryValue;

    /**
     * 字典数据对应的补充数据Value值，如果除了primaryValue业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findByPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "次要数据")
    @JsonView(JsonViews.List.class)
    private String secondaryValue;

    /**
     * 字典数据对应的补充文件类型Value值，页面以文件组件方式维护
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findByPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "文件路径数据")
    @JsonView(JsonViews.List.class)
    @Column(length = 512)
    private String filePathValue;
    
    /**
     * 字典数据对应的补充图片类型Value值，页面以多图组件方式维护
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findByPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "图片路径数据")
    @JsonView(JsonViews.List.class)
    @Column(length = 1024)
    private String imagePathValue;

    /**
     * 字典数据对应的补充数据大文本类型Value值，如果除了primaryValue业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findByPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "大文本数据", tooltips = "以CLOB大文本方式存储用于特定的大文本数据配置")
    @Lob
    @JsonView(JsonViews.Detail.class)
    private String richTextValue;

    @MetaData(value = "禁用标识", tooltips = "禁用项目全局不显示")
    @JsonView(JsonViews.Admin.class)
    private Boolean disabled = Boolean.FALSE;

    @MetaData(value = "排序号", tooltips = "相对排序号，数字越大越靠上显示")
    @JsonView(JsonViews.Admin.class)
    private Integer orderRank = 10;

    @MetaData(value = "父节点")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "none"))
    @JsonSerialize(using = EntityIdDisplaySerializer.class)
    @JsonView(JsonViews.Admin.class)
    private DataDict parent;

    @MetaData(value = "子节点集合")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("orderRank desc")
    @JsonIgnore
    private List<DataDict> children;

    @Override
    @Transient
    public String getDisplay() {
        return primaryKey + ":" + primaryValue;
    }

    @Transient
    @JsonIgnore
    public String getUniqueKey() {
        StringBuilder sb = new StringBuilder();
        if (parent != null) {
            sb.append(parent.getPrimaryKey() + "_");
        }
        sb.append(primaryKey);
        if (StringUtils.isNotBlank(secondaryKey)) {
            sb.append("_" + secondaryKey);
        }
        return sb.toString();
    }
}
