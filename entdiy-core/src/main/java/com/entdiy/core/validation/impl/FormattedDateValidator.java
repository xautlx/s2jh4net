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
package com.entdiy.core.validation.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.entdiy.core.validation.FormattedDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class FormattedDateValidator implements ConstraintValidator<FormattedDate, Object> {

    String pattern;

    @Override
    public void initialize(FormattedDate formattedDate) {
        this.pattern = formattedDate.pattern();
        if (StringUtils.isBlank(this.pattern)) {
            switch (formattedDate.iso()) {
            case DATE:
                this.pattern = "yyyy-MM-dd";
                break;
            case TIME:
                this.pattern = "hh:mm:ss";
                break;
            case DATE_TIME:
                this.pattern = "yyyy-MM-dd hh:mm:ss";
                break;
            default:
                break;
            }
        }
        Assert.notNull(this.pattern);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String data = String.valueOf(value);
        if (StringUtils.isBlank(data)) {
            return true;
        }
        return isDate(data, pattern);
    }

    private static boolean isDate(String value, String format) {
        SimpleDateFormat sdf = null;
        ParsePosition pos = new ParsePosition(0);//指定从所传字符串的首位开始解析  
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            Date date = sdf.parse(value, pos);
            if (date == null) {
                return false;
            } else {
                //更为严谨的日期,如2011-03-024认为是不合法的  
                if (pos.getIndex() > sdf.format(date).length()) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
