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
package com.entdiy.core.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 按照Grid组件及API接口分页数据结构，定义一个特定的分页对象用于JSON输出，
 * 避免对默认的 @see org.springframework.data.domain.PageImpl 大量属性JSON渲染不必要的处理
 *
 * @param <T>
 */
public class JsonPage<T> {

    private Page<T> page;

    public JsonPage(Page<T> page) {
        this.page = page;
    }

    /**
     * 当前页码，转换为从1开始
     *
     * @return
     */
    public Integer getPageNumber() {
        return page.getPageable().getPageNumber() + 1;
    }

    /**
     * 当前页分页数据集合
     *
     * @return
     */
    public List<T> getContent() {
        return page.getContent();
    }


    /**
     * 总页数
     *
     * @return
     */
    public Integer getTotalPages() {
        return page.getTotalPages();
    }

    /**
     * 总记录数
     *
     * @return
     */
    public Long getTotalElements() {
        return page.getTotalElements();
    }

    /**
     * 标识已经没有更多数据
     *
     * @return
     */
    public boolean isLast() {
        return page.isLast();
    }
}
