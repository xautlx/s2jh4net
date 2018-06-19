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
import com.entdiy.core.entity.BaseAttachmentFile;
import com.entdiy.core.entity.EnumKeyLabelPair;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
        LOCAL_READ {
            @Override
            public String getLabel() {
                return "应用本地读取";
            }
        },

        /**
         * 适用于开放式文件类型访问，诸如图片，应用服务器上传文件目录与Nginx配置共享目录，
         * 应用上传的文件转换为由Nginx proxy_pass代理地址，提高文件访问效率
         * 对外访问地址形如：http://local.app.com/contextpath/upload/XXX
         */
        @MetaData(value = "对外返回静态资源服务器代理地址")
        LOCAL_PROXY {
            @Override
            public String getLabel() {
                return "本地代理访问";
            }
        },

        /**
         * 适用于开放式文件类型访问，诸如图片，直接调用第三方的CDN服务接口上传文件，
         * 对于诸如开放图片之类的访问，在有条件的情况下尽可能采用此方式
         * 对外访问地址形如：http://cdn.server.com/app/upload/XXX
         */
        @MetaData(value = "CDN内容分发绝对URL地址")
        CDN {
            @Override
            public String getLabel() {
                return "CDN内容分发";
            }
        }
    }

    @Transient
    public String getAccessUrl() {
        //假如是以外部CDN形式存取文件，则组装CDN访问路径；否则本地应用存储形式，返回应用访问地址
        if (AccessModeEnum.CDN.equals(this.accessMode)) {
            return storePrefix + relativePath;
        } else {
            String uri = AppContextHolder.getWebContextUri();

            //兼容处理通过Nginx+Ngrok多层穿透代理无法正确获取上下文URL，直接返回相对路径
            if (AppContextHolder.isDevMode() || AppContextHolder.isDemoMode()) {
                if (uri.indexOf("localhost") > -1) {
                    return "/pub/file/view/" + getId();
                }
            }

            if (AccessModeEnum.LOCAL_PROXY.equals(this.accessMode)) {
                return uri + relativePath;
            } else {
                //正常情况返回绝对路径，方便API接口使用
                return uri + "/pub/file/view/" + getId();
            }
        }
    }
}
