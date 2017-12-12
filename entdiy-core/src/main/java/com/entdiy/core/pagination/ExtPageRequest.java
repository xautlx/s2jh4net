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
package com.entdiy.core.pagination;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 扩展Spring Data定义的分页对象，添加精确的offset控制属性。
 * @see {@link PropertyFilter#buildPageableFromHttpRequest(javax.servlet.http.HttpServletRequest)}
 */
public class ExtPageRequest extends PageRequest {

    private static final long serialVersionUID = 7944779254954509445L;

    private int offset = -1;

    public ExtPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public ExtPageRequest(int offset, int page, int size, Sort sort) {
        super(page, size, sort);
        this.offset = offset;
    }

    @Override
    public int getOffset() {
        if (offset > -1) {
            return offset;
        } else {
            return super.getOffset();
        }
    }

    /**
     * 一般用于把没有分页的集合数据转换组装为对应的Page对象，传递到前端Grid组件以统一的JSON结构数据显示
     * @param list 泛型集合数据
     * @return 转换封装的Page分页结构对象
     */
    public static <S> Page<S> buildPageResultFromList(List<S> list) {
        Page<S> page = new PageImpl<S>(list);
        return page;
    }
}
