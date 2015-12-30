package lab.s2jh.module.auth.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "auth_SignupUser")
@MetaData(value = "自助注册账号数据")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class SignupUser extends BaseNativeEntity {

    private static final long serialVersionUID = -1802915812231452200L;

    @MetaData(value = "账号全局唯一标识", comments = "同时作为SYS类型用户登录密码的SALT")
    @Column(length = 64, nullable = false, unique = true)
    private String authGuid;

    @MetaData(value = "登录账号")
    @Size(min = 3, max = 30)
    @Column(length = 128, unique = true, nullable = false)
    private String authUid;

    @MetaData(value = "登录密码")
    @Column(updatable = false, length = 128, nullable = false)
    private String password;

    @MetaData(value = "真实姓名")
    @Column(length = 64)
    private String trueName;

    @MetaData(value = "昵称")
    @Column(length = 64)
    private String nickName;

    @MetaData(value = "电子邮件")
    @Email
    @Column(length = 128)
    private String email;

    @MetaData(value = "移动电话", tooltips = "请仔细填写，可用于系统通知短信发送，找回密码等功能")
    private String mobile;

    @MetaData(value = "注册时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    private Date signupTime;

    @MetaData(value = "备注说明")
    @Column(length = 3000)
    private String remarkInfo;

    @MetaData(value = "审核处理时间")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    private Date auditTime;

    @JsonIgnore
    @Transient
    private User user;
}
