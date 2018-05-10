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
import com.entdiy.core.entity.BaseUuidEntity;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.support.web.filter.RequestContextFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
public class AttachmentFile extends BaseUuidEntity {

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

    @MetaData(value = "是否CDN存储模式")
    @Column(length = 512, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private Boolean storeCdnMode;

    @Transient
    public String getAccessUrl() {
        if (this.isNew()) {
            return null;
        }
        //假如是以外部CDN形式存取文件，则组装CDN访问路径；否则本地应用存储形式，返回应用访问地址
        if (storeCdnMode) {
            return storePrefix + relativePath;
        } else {
            return RequestContextFilter.getFullContextURL() + "/pub/file/view/" + getId();
        }
    }
}
