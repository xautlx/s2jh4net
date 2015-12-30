package lab.s2jh.module.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.web.json.JsonViews;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonView;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_ConfigProperty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "配置属性")
@Audited
public class ConfigProperty extends BaseNativeEntity {

    private static final long serialVersionUID = 8136580659799847607L;

    @MetaData(value = "代码")
    @Column(length = 64, unique = true, nullable = false)
    private String propKey;

    @MetaData(value = "名称")
    @Column(length = 256, nullable = false)
    private String propName;

    @MetaData(value = "简单属性值")
    @Column(length = 256)
    private String simpleValue;

    @MetaData(value = "HTML属性值")
    @Lob
    @JsonView(JsonViews.AppDetail.class)
    private String htmlValue;

    @MetaData(value = "参数属性用法说明")
    @Column(length = 2000)
    private String propDescn;

    @Override
    @Transient
    public String getDisplay() {
        return propKey;
    }
}
