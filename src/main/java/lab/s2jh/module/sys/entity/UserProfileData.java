package lab.s2jh.module.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.cons.GlobalConstant;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.web.json.EntityIdDisplaySerializer;
import lab.s2jh.module.auth.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_UserProfileData", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "code" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "用户配置数据")
public class UserProfileData extends BaseNativeEntity {

    private static final long serialVersionUID = -3203959719354074416L;

    @MetaData(value = "用户")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = GlobalConstant.GlobalForeignKeyName))
    @JsonSerialize(using = EntityIdDisplaySerializer.class)
    private User user;

    @MetaData(value = "代码")
    @Column(length = 128, nullable = false)
    private String code;

    @MetaData(value = "参数值")
    @Column(length = 128, nullable = true)
    private String value;
}
