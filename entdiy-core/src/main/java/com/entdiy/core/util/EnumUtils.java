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
package com.entdiy.core.util;

import com.entdiy.core.entity.EnumKeyLabelPair;
import com.google.common.collect.Maps;

import java.util.Map;

public class EnumUtils {

    private static Map<Class<?>, Map<Enum<?>, String>> enumDatasContainer = Maps.newHashMap();

    /**
     * 基于Enum类返回对应的key-value Map构建对象
     * @param enumClass
     * @return
     */
    public static Map<Enum<?>, String> getEnumDataMap(Class<? extends Enum> enumClass) {
        Map<Enum<?>, String> enumDataMap = enumDatasContainer.get(enumClass);
        if (enumDataMap == null) {
            Enum[] enums = enumClass.getEnumConstants();
            enumDataMap = Maps.newHashMap();
            for (Enum item : enums) {
                if (item instanceof EnumKeyLabelPair) {
                    EnumKeyLabelPair kl = (EnumKeyLabelPair) item;
                    enumDataMap.put(item, kl.getLabel());
                } else {
                    enumDataMap.put(item, item.name());
                }
            }
        }
        return enumDataMap;
    }

}
