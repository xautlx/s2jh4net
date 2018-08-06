/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.sys.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.context.SpringPropertiesHolder;
import com.entdiy.core.entity.BaseAttachmentFile;
import com.entdiy.core.entity.EnumKeyLabelPair;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_AttachmentFile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "上传文件")
public class AttachmentFile extends BaseAttachmentFile {

    @MetaData(value = "所属对象Class")
    @Column(length = 512, nullable = true)
    @JsonView(JsonViews.Admin.class)
    private String sourceType;

    @MetaData(value = "所属对象ID")
    @Column(length = 64, nullable = true)
    @JsonView(JsonViews.Admin.class)
    private String sourceId;

    @MetaData(value = "所属分类", comments = "同一个对象类型下面，附件分类，如产品图片，参考文档等")
    @Column(length = 256, nullable = true)
    @JsonView(JsonViews.Admin.class)
    private String sourceCategory = GlobalConstant.DEFAULT_VALUE;

    @MetaData(value = "显示排序号", tooltips = "相对排序号，取集合索引下标，数字越大越靠后显示")
    @Column(nullable = true)
    @JsonView(JsonViews.Admin.class)
    private Integer orderIndex = 0;

    @MetaData(value = "附件上传文件名称")
    @Column(length = 512, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private String fileRealName;

    @MetaData(value = "文件描述")
    @Column(length = 2048, nullable = true)
    @JsonIgnore
    private String fileDescription;

    @MetaData(value = "附件扩展名")
    @Column(length = 32, nullable = true)
    @JsonView(JsonViews.Admin.class)
    private String fileExtension;

    @MetaData(value = "附件大小")
    @Column(nullable = false)
    @JsonView(JsonViews.Admin.class)
    private Long fileLength;

    @MetaData(value = "附件MIME类型")
    @Column(length = 256, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private String fileContentType;

    @MetaData(value = "相对存储路径")
    @Column(length = 512, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private String relativePath;

    @MetaData(value = "存储路径前缀", comments = "如果是应用本地存储则存放文件所在磁盘路径前缀；如果是CDN网上存储则存放HTTP访问前缀")
    @Column(length = 512, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private String storePrefix;

    @MetaData(value = "文件访问模式")
    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.Admin.class)
    @ApiModelProperty(hidden = true)
    private AccessModeEnum accessMode;

    public enum AccessModeEnum implements EnumKeyLabelPair {
        /**
         * 适用于对权限检查严格的文件访问，限制无法通过开放地址直接访问到文件
         * 文件读取由本地java服务器处理，对IO和CPU资源占用损耗，除非文件访问有严格限制，建议尽量不采用此方式
         * 对外访问地址形如：http://local.app.com/contextpath/pub/file/XXXX
         */
        @MetaData(value = "直接从本地文件系统IO读取返回响应", comments = "")
        PRIVATE {
            @Override
            public String getLabel() {
                return "受限访问资源";
            }
        },

        /**
         * 适用于开放式文件类型访问，诸如图片，Nginx配置共享访问目录或直接调用第三方的CDN服务接口上传文件
         * 对外访问地址形如：http://cdn.server.com/app/upload/XXX
         */
        @MetaData(value = "CDN内容分发绝对URL地址")
        PUBLIC {
            @Override
            public String getLabel() {
                return "开放访问资源";
            }
        }
    }

    private static RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    @Transient
    public String getAccessUrl() {
        if (AccessModeEnum.PRIVATE.equals(this.accessMode)) {
            return "/pub/file/view/" + getId();
        } else {
            return getAccessUrl(relativePath);
        }
    }

    /**
     * 基于当前相对URI，返回其开放访问地址网址URL
     *
     * @param relativePath
     * @return
     */
    public static String getAccessUrl(String relativePath) {
        String uriProps = SpringPropertiesHolder.getProperty(GlobalConstant.CFG_UPLOAD_PUBLIC_RESOURCE_URI);
        String[] uris = StringUtils.split(uriProps, ",");
        String uri = uris.length > 1 ? uris[randomDataGenerator.nextInt(0, uris.length - 1)] : uris[0];
        return uri + relativePath;
    }
}
