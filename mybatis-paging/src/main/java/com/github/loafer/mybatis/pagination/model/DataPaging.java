package com.github.loafer.mybatis.pagination.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date Created 2014-2-26
 *
 * @author loafer[zjh527@163.com]
 * @version 1.0
 */
public class DataPaging<E> {
    private final List<E> rows = new ArrayList<E>();
    private final int total;

    public DataPaging(Collection<? extends E> content, int total){
        this.rows.addAll(content);
        this.total = total;
    }

    public List<E> getRows(){
        return rows;
    }

    public int getTotal(){
        return total;
    }

    @Override
    public String toString() {
        return "DataPaging{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
