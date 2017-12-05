package com.entdiy.core.web;

/**
 * 用于批量数据单一实体处理逻辑匿名回调接口
 * @param <T>
 */
public interface EntityProcessCallbackHandler<T> {

    void processEntity(T entity) throws EntityProcessCallbackException;

    public class EntityProcessCallbackException extends Exception {

        private static final long serialVersionUID = -2803641078892909145L;

        public EntityProcessCallbackException(String msg) {
            super(msg);
        }
    }
}
