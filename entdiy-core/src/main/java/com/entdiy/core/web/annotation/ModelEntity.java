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
package com.entdiy.core.web.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注解参数对象自动基于id获取entity绑定到容器
 *
 * @see com.entdiy.core.web.method.ModelEntityMethodProcessor
 * @see org.springframework.web.bind.annotation.ModelAttribute
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelEntity {

    String KEY_ENTITY = "entity";

    String KEY_VALIDATION_RULES = "validationRules";


    /**
     * Alias for {@link #name}.
     */
    @AliasFor("name")
    String value() default KEY_ENTITY;

    /**
     * The name of the model attribute to bind to.
     * <p>The default model attribute name is inferred from the declared
     * attribute type (i.e. the method parameter type or method return type),
     * based on the non-qualified class name:
     * e.g. "orderAddress" for class "mypackage.OrderAddress",
     * or "orderAddressList" for "List&lt;mypackage.OrderAddress&gt;".
     *
     * @since 4.3
     */
    @AliasFor("value")
    String name() default KEY_ENTITY;


    /**
     * Allows declaring data binding disabled directly on an {@code @ModelAttribute}
     * method parameter or on the attribute returned from an {@code @ModelAttribute}
     * method, both of which would prevent data binding for that attribute.
     * <p>By default this is set to {@code true} in which case data binding applies.
     * Set this to {@code false} to disable data binding.
     *
     * @since 4.3
     */
    boolean binding() default true;

    /**
     * 自动构建实体对象校验规则JSON字符串存储到Model的Key名称。
     * 设置为 {@link com.entdiy.core.cons.GlobalConstant#NONE_VALUE} 标识不生成。
     */
    String validateRules() default KEY_VALIDATION_RULES;


    /**
     * 声明需要为Detach使用的Lazy加载属性名称列表
     *
     * @return
     */
    String[] preFectchLazyFields() default {};

    /**
     * 为了防止用户恶意传入数据修改不可访问的属性，采用白名单机制，只有在该方法中定义的属性才会进行自动绑定
     * 请记住把所有表单元素涉及到属性添加到此方法的setAllowedFields列表中，否则会出现页面数据没有正确保存到数据库
     *
     * @see org.springframework.web.bind.WebDataBinder#setAllowedFields(String...)
     */
    String[] allowedFields() default {};

    /**
     * @see org.springframework.web.bind.WebDataBinder#setDisallowedFields(String...)
     */
    String[] disallowedFields() default {};


    boolean byAuthAccount() default false;
}
