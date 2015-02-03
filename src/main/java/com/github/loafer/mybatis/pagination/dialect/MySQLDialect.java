package com.github.loafer.mybatis.pagination.dialect;

/**
 * Date Created  2014-2-18
 *
 * @author loafer[zjh527@163.com]
 * @version 1.0
 */
public class MySQLDialect extends Dialect{
    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(final String sql, final int offset, final int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    private String getLimitString(final String sql, final int offset,
                                  final String offsetPlaceholder, final String limitPlaceholder){
        StringBuilder stringBuilder = new StringBuilder(getLineSql(sql));
        stringBuilder.append(" limit ");
        if(offset > 0){
            stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
        }else{
            stringBuilder.append(limitPlaceholder);
        }

        return stringBuilder.toString();
    }
}
