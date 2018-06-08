/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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
package com.entdiy.locale.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.context.SpringContextHolder;
import com.entdiy.core.util.reflection.ReflectionUtils;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.support.web.filter.RequestContextFilter;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.service.DataDictService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 对于一些需要从数据层面支持国际化的应用，以嵌套属性的形式对属性值进行多国语言设置处理
 * 并基于前端传入的国际化标识，返回对应国际化数据值
 *
 * @see RequestContextFilter
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class LocalizedData {

    public final static String DataDict_Locales = "DataDict_Locales";

    @MetaData("简体中文")
    private String zhCN;

    @MetaData("繁体中文")
    private String zhTW;

    @MetaData("英语")
    private String enUS;

    @MetaData("日语")
    private String jaJP;

    @JsonView(JsonViews.App.class)
    public String getLocalizedText() {
        String locale = RequestContextFilter.getRequestLocalePropName();
        Object val = ReflectionUtils.invokeGetterMethod(this, locale);
        //容错处理，如果指定语言未配置数据，则返回默认的中文数据
        if (val == null) {
            return getZhCN();
        }
        String str = val.toString();
        if (StringUtils.isBlank(str)) {
            return getZhCN();
        } else {
            return str;
        }
    }

    @JsonIgnore
    public List<LocalizedDataItem> getItems() {
        List<LocalizedDataItem> items = Lists.newArrayList();
        DataDictService dataDictService = SpringContextHolder.getBean(DataDictService.class);
        List<DataDict> locales = dataDictService.findChildrenByRootPrimaryKey(LocalizedData.DataDict_Locales);
        for (DataDict locale : locales) {
            //移除中横线以匹配对应属性名称
            String key = StringUtils.remove(locale.getPrimaryKey(), "-");
            Object val = ReflectionUtils.invokeGetterMethod(this, key);
            LocalizedDataItem item = new LocalizedDataItem();
            item.setKey(key);
            item.setName(locale.getPrimaryValue());
            String required = locale.getSecondaryValue();
            item.setRequired(StringUtils.isNotBlank(required) ? required : "false");
            item.setValue(val == null ? null : val.toString());
            items.add(item);
        }
        return items;
    }

    @Setter
    @Getter
    public static class LocalizedDataItem {
        private String key;
        private String name;
        private String value;
        private String required;
    }
}
