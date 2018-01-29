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
package com.entdiy.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基于Spring DATA JPA的树形结构对象投影接口定义
 *
 * @see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
 */
public interface TreeDataDto {
    /** 节点ID值 */
    @JsonProperty
    String getId();

    /** 节点显示字面值 */
    @JsonProperty
    String getDisplay();

    /** 有效的子节点个数，用于直观展示当前节点是否叶子节点 */
    @JsonProperty
    Integer getChildrenCount();

    /** 节点路径层次表达式（预留，后端暂不支持处理） */
    String getPath();
}
