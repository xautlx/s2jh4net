package lab.s2jh.module.auth.entity;

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
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_Department")
@MetaData(value = "部门")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department extends BaseNativeEntity implements Comparable<Department> {

    private static final long serialVersionUID = -7634994834209530394L;

    @MetaData(value = "代码", comments = "代码本身具有层级信息，用于进行从属权限控制")
    @Size(min = 3)
    @Column(nullable = false, length = 255, unique = true)
    private String code;

    @MetaData(value = "名称")
    @Column(nullable = false, length = 32)
    private String name;

    @MetaData(value = "父节点")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "none"))
    @JsonIgnore
    private Department parent;

    @Override
    @Transient
    public String getDisplay() {
        return code + " " + name;
    }

    @Transient
    public String getPathDisplay() {
        Department category = this;
        String label = this.getName();
        while (category.getParent() != null) {
            label = category.getParent().getName() + "/" + label;
            category = category.getParent();
        }
        return label;
    }

    @Override
    public int compareTo(Department o) {
        return CompareToBuilder.reflectionCompare(o.getCode(), this.getCode());
    }
}
