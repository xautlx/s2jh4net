package lab.s2jh.module.sys.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sys_AttachmentFile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "附件文件数据")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public class AttachmentFile extends BaseEntity<String> {

    private static final long serialVersionUID = -6042357698510260065L;

    /** 附件上传文件名称 */
    private String fileRealName;

    /** 文件描述 */
    private String fileDescription;

    /** 附件扩展名 */
    private String fileExtension;

    /** 附件大小 */
    private Long fileLength;

    private String fileRelativePath;

    private String id;

    /** 直接以文件byte数据计算的MD5码或UUID作为唯一标识 */
    @Id
    @Column(length = 80)
    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(length = 500, nullable = false)
    @JsonProperty
    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    @Column(nullable = false)
    @JsonProperty
    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    @Column(length = 8)
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Column(length = 200, nullable = true)
    @JsonIgnore
    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    @Override
    @Transient
    public String getDisplay() {
        return fileRealName;
    }

    @Transient
    @JsonIgnore
    public static AttachmentFile buildInstance(String fileName, Long fileLength) {
        //简便的做法用UUID作为主键，每次上传都会创建文件对象和数据记录，便于管理，但是存在相同文件重复保存情况
        String id = UUID.randomUUID().toString();

        DateTime now = new DateTime();
        StringBuilder sb = new StringBuilder();
        int year = now.getYear();
        sb.append("/" + year);
        String month = "";
        int monthOfYear = now.getMonthOfYear();
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        } else {
            month = "" + monthOfYear;
        }
        String day = "";
        int dayOfMonth = now.getDayOfMonth();
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = "" + dayOfMonth;
        }
        sb.append("/" + month);
        sb.append("/" + day);
        Assert.notNull(id, "id is required to buildInstance");
        sb.append("/" + StringUtils.substring(id, 0, 2));
        String path = sb.toString();

        AttachmentFile af = new AttachmentFile();
        af.setId(path.replaceAll("/", "") + id);
        af.setFileRelativePath(path);
        af.setFileLength(fileLength);
        af.setFileRealName(fileName);
        af.setFileExtension(StringUtils.substringAfterLast(fileName, "."));
        return af;
    }

    public String getFileRelativePath() {
        return fileRelativePath;
    }

    public void setFileRelativePath(String fileRelativePath) {
        this.fileRelativePath = fileRelativePath;
    }

    @Transient
    @JsonIgnore
    public String getDiskFileName() {
        return getId() + "." + getFileExtension();
    }
}
