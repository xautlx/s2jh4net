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
import com.entdiy.core.entity.BaseUuidEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "source_type", length = 512, nullable = true)
    private String sourceType;

    @MetaData(value = "所属对象ID")
    @Column(name = "source_id", nullable = true)
    private Long sourceId;

    @MetaData(value = "附件上传文件名称")
    @Column(length = 512, nullable = false)
    private String fileRealName;

    @MetaData(value = "文件描述")
    @Column(length = 2048, nullable = true)
    @JsonIgnore
    private String fileDescription;

    @MetaData(value = "附件扩展名")
    @Column(length = 32, nullable = true)
    private String fileExtension;

    @MetaData(value = "附件大小")
    @Column(nullable = false)
    private Long fileLength;

    @MetaData(value = "附件MIME类型")
    @Column(length = 32, nullable = false)
    private String fileContentType;

    @MetaData(value = "相对存储路径")
    @Column(length = 512, nullable = false)
    private String relativePath;

    @MetaData(value = "存储绝对路径", comments = "记录参考之用，一般业务功能不做使用")
    @Column(length = 512, nullable = false)
    private String absolutePath;
}
