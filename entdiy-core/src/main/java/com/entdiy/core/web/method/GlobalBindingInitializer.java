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
package com.entdiy.core.web.method;

import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.json.LocalDateSerializer;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class GlobalBindingInitializer {

    @InitBinder
    public void registerCustomEditors(WebDataBinder binder) {

        //自定义类型属性转换，动态处理日期、时间等不同格式数据转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(DateUtils.parseDate(text));
                }
            }

            @Override
            public String getAsText() {
                Date value = (Date) getValue();
                return (value != null ? DateUtils.formatDate(value) : "");
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text, LocalDateSerializer.LOCAL_DATE_FORMATTER));
                }
            }

            @Override
            public String getAsText() {
                LocalDate value = (LocalDate) getValue();
                return (value != null ? value.format(LocalDateSerializer.LOCAL_DATE_FORMATTER) : "");
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(LocalDateTime.parse(text, LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER));
                }
            }

            @Override
            public String getAsText() {
                LocalDateTime value = (LocalDateTime) getValue();
                return (value != null ? value.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER) : "");
            }
        });

        // Converts empty strings into null when a form is submitted
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    }
}
