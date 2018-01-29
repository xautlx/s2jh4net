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
package com.entdiy.core.entity.def;

import com.entdiy.core.annotation.MetaData;

public enum DynamicParameterTypeEnum {

    @MetaData(value = "日期", comments = "不带时分的年月日日期,格式yyyy-MM-dd")
    DATE,

    @MetaData(value = "日期时间", comments = "带时分的日期,格式：yyyy-MM-dd HH:mm:ss")
    TIMESTAMP,

    @MetaData("浮点数")
    FLOAT,

    @MetaData("整数")
    INTEGER,

    @MetaData("是否布尔型")
    BOOLEAN,

    @MetaData(value = "枚举数据定义", comments = "根据枚举对象的name()和getLabel()返回对应的key1-value1结构数据,对应的listDataSource写法示例：com.entdiy.demo.po.entity.PurchaseOrder$PurchaseOrderTypEnum")
    ENUM,

    @MetaData(value = "数据字典下拉列表", comments = "提供数据字典表中CATEGORY对应的key1-value1结构数据,对应的listDataSource写法示例：PRIVILEGE_CATEGORY")
    DATA_DICT_LIST,

    @MetaData(value = "SQL查询下拉列表", comments = "直接以SQL语句形式返回key-value结构数据,对应的listDataSource写法示例：select role_code,role_name from t_sys_role")
    SQL_LIST,

    @MetaData(value = "OGNL语法键值对", comments = "以OGNL语法构造key-value结构数据,对应的listDataSource写法示例：#{'A':'ClassA','B':'ClassB'}")
    OGNL_LIST,

    @MetaData("多行文本")
    MULTI_TEXT,

    @MetaData("HTML文本")
    HTML_TEXT,

    @MetaData("单行文本")
    STRING;

}
