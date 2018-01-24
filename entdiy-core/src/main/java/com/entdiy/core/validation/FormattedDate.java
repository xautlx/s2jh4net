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
package com.entdiy.core.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.entdiy.core.validation.impl.FormattedDateValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormattedDateValidator.class)
@Documented
public @interface FormattedDate {
    String message() default "不符合日期格式:{pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * The ISO pattern to use to format the field.
     * The possible ISO patterns are defined in the {@link ISO} enum.
     * Defaults to ISO.NONE, indicating this attribute should be ignored.
     * Set this attribute when you wish to format your field in accordance with an ISO date time format.
     */
    ISO iso() default ISO.NONE;

    /**
     * The custom pattern to use to format the field.
     * Defaults to empty String, indicating no custom pattern String has been specified.
     * Set this attribute when you wish to format your field in accordance with a custom date time pattern not represented by a style or ISO format.
     */
    String pattern() default "";

    /**
     * Common ISO date time format patterns.
     */
    public enum ISO {

        /** 
         * The most common ISO Date Format <code>yyyy-MM-dd</code> e.g. 2000-10-31.
         */
        DATE,

        /** 
         * The most common ISO Time Format <code>hh:mm:ss.SSSZ</code> e.g. 01:30:00.000-05:00.
         */
        TIME,

        /** 
         * The most common ISO DateTime Format <code>yyyy-MM-dd'T'hh:mm:ss.SSSZ</code> e.g. 2000-10-31 01:30:00.000-05:00.
         * The default if no annotation value is specified.
         */
        DATE_TIME,

        /**
         * Indicates that no ISO-based format pattern should be applied.
         */
        NONE
    }
}