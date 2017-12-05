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
