package com.github.loafer.mybatis.pagination.helper;

import com.github.loafer.mybatis.pagination.dialect.Dialect;
import com.github.loafer.mybatis.pagination.dialect.DialectFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Date Created  2014-2-18
 *
 * @author loafer[zjh527@163.com]
 * @version 2.0
 */
public abstract class DialectHelper {
    private static Map<Dialect.Type, Dialect> MAPPERS = new HashMap<Dialect.Type, Dialect>();

    public static Dialect getDialect(Dialect.Type dialectType){
        if(MAPPERS.containsKey(dialectType)){
            return MAPPERS.get(dialectType);
        }else{
            Dialect dialect = DialectFactory.buildDialect(dialectType);
            MAPPERS.put(dialectType, dialect);
            return dialect;
        }
    }
}
