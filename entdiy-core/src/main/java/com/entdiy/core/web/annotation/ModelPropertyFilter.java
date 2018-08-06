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
 * 注解参数对象自动基于request请求构建查询参数对象
 *
 * @see ModelPropertyFilterMethodProcessor
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelPropertyFilter {

    /**
     * Alias for {@link #clazz()}.
     */
    @AliasFor("clazz")
    Class<?> value() default void.class;

    /**
     * Alias for {@link #value()}.
     */
    @AliasFor("value")
    Class<?> clazz() default void.class;

    /**
     * 对于一些操作需要做数据访问检查，以确保当前登录用户只能访问关联的数据。
     * 指定需要进行数据访问检查的实体属性名称，处理器自动基于当前登录用户User对象与此属性进行比对，断言访问控制
     * 注意：指定实体属性名对应的类型必须是 com.entdiy.auth.entity.User ，否则抛出异常
     *
     */
    String dataAccessControl() default "";
}
