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

import org.springframework.data.domain.Sort;

import java.lang.annotation.*;

/**
 * 注解参数对象自动基于request请求构建分页排序参数对象
 *
 * @see com.entdiy.core.web.method.ModelPageableRequestMethodProcessor
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelPageableRequest {

    int rows() default 5;

    String sortProperty() default "";

    Sort.Direction sortDirection() default Sort.Direction.DESC;
}
