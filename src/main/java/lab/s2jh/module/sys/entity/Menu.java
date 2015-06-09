package lab.s2jh.module.sys.entity;

import java.lang.reflect.Method;
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
import lab.s2jh.core.util.Exceptions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_Menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "菜单")
@Audited
public class Menu extends BaseNativeEntity {

    private static final long serialVersionUID = 2860233299443173932L;

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

    @MetaData(value = "缓存Web Controller调用方法", comments = "用于缓存记录对应的Controller方法，方便权限判断比较")
    @Transient
    private Method mappingMethod;

    @Transient
    @JsonIgnore
    public Method getMappingMethod() {
        if (mappingMethod != null) {
            return mappingMethod;
        }
        //基于记录的Controller类和方法信息构造MethodInvocation,用于后续调用shiro的拦截器进行访问权限比对
        if (StringUtils.isNotBlank(getControllerMethod())) {

            try {
                final Class<?> clazz = ClassUtils.getClass(getControllerClass());
                Method[] methods = clazz.getMethods();
                for (final Method method : methods) {
                    if (method.getName().equals(getControllerMethod())) {
                        RequestMapping rm = method.getAnnotation(RequestMapping.class);
                        if (rm.method() == null || rm.method().length == 0 || ArrayUtils.contains(rm.method(), RequestMethod.GET)) {
                            mappingMethod = method;
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                Exceptions.unchecked(e);
            }
        }
        return mappingMethod;
    }
}
