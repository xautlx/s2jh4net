/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 标识对象支持关联附件处理，由框架自动处理附件相关绑定和清理工作
 * 需要支持附件关联的实体对象实现该接口，定义一个属性存放关联附件个数
 */
public interface AttachmentableEntity {

    /**
     * 关联附件个数
     * @return
     */
    @JsonProperty
    public Integer getAttachmentSize();
}
