package com.github.loafer.mybatis.pagination.dialect;

import com.github.loafer.mybatis.pagination.PaginationInterceptor;
import org.apache.commons.lang3.StringUtils;

/**
 * Date Created  2017-12-18
 *
 * @author loafer[mzh]
 * @author lixia[xautlx@hotmail.com]
 *
 * @version 1.0
 */
public class MSSQLDialect extends Dialect {
    @Override
    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset, int limit) {
        String[] splits = PaginationInterceptor.splitOrderBy(sql);
        String sqlWithoutOrderBy = splits[0];
        String orderBy = splits.length > 1 ? splits[1] : "select(1) asc";
        //拼接主语句 top
        String strSql = StringUtils.substringAfter(sqlWithoutOrderBy, "select");
        String selectSql = "select top (" + limit + ") * from " +
                "(select TOP (100) PERCENT ROW_NUMBER() OVER (order by" + orderBy + ") AS RowNumber," + strSql + ") as temp_table " +
                "where  RowNumber > " + offset;

        StringBuilder stringBuilder = new StringBuilder(getLineSql(selectSql));

        return stringBuilder.toString();
    }
}
