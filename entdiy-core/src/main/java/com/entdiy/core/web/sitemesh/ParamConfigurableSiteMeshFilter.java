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
package com.entdiy.core.web.sitemesh;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;

/**
 * 扩展实现注入基于request参数decorator值进行动态定位装饰器的选择器
 */
public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        //获取原有默认配置装饰选择器
        DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();
        //赋给自定义装饰选择器，则自定义规则未匹配时调用默认选择器获取
        builder.setCustomDecoratorSelector(new ParamDecoratorSelector(defaultDecoratorSelector));
    }
}
