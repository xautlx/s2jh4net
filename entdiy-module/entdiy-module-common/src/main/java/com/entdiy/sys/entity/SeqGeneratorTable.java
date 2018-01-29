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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于 @see TableSeqGenerator 所需的数据表结构定义之用
 */
@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "seq_generator_table")
public class SeqGeneratorTable {

    @Id
    private String id;

    @Column(name = "initial_value")
    private Integer initialValue;

    @Column(name = "increment_size")
    private Integer incrementSize;

    @Column(name = "next_val")
    private Long nextVal;

}
