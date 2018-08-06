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
package com.entdiy.core.web;

import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.exception.BaseRuntimeException;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.web.view.OperationResult;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public abstract class BaseController<T extends AbstractPersistableEntity, ID extends Serializable> {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected OperationResult editSave(BaseService baseService, T entity) {
        baseService.save(entity);
        Map<String, Object> result = Maps.newHashMap();
        result.put("id", entity.getId());
        return OperationResult.buildSuccessResult("数据保存处理完成", result);
    }

    protected OperationResult delete(BaseService baseService, T... entities) {
        Assert.notNull(entities, "ids参数不能为空");
        return delete(baseService, entities, null);
    }

    protected OperationResult delete(BaseService baseService, T[] entities, EntityProcessCallbackHandler<T> handler) {
        //删除失败的id和对应消息以Map结构返回，可用于前端批量显示错误提示和计算表格组件更新删除行项
        Map<Object, String> errorMessageMap = Maps.newLinkedHashMap();
        Set<T> enableDeleteEntities = Sets.newHashSet();

        for (T entity : entities) {
            String msg = null;
            //回调接口调用，比如以内部类方式传入对象是否可删除的检测逻辑
            if (handler != null) {
                msg = handler.processEntity(entity);
            }
            if (StringUtils.isBlank(msg)) {
                enableDeleteEntities.add(entity);
            } else {
                errorMessageMap.put(entity.getId(), msg);
            }
        }
        //对于批量删除,循环每个对象调用Service接口删除,则各对象删除事务分离
        //这样可以方便某些对象删除失败不影响其他对象删除
        //如果业务逻辑需要确保批量对象删除在同一个事务则请子类覆写调用Service的批量删除接口
        for (T entity : enableDeleteEntities) {
            try {
                baseService.delete(entity);
            } catch (Exception e) {
                String errorMessage = null;
                if (e instanceof DataIntegrityViolationException) {
                    Throwable cause = e;
                    while (cause != null) {
                        String sqlMessage = cause.getMessage();
                        if (sqlMessage.indexOf("FK") > -1) {
                            //提取外键名称，方便用户反馈问题是排查告知具体关联数据逻辑问题
                            //for MySQL
                            errorMessage = "该数据已被关联使用：FK" + StringUtils.substringBetween(sqlMessage, "FK", "`");
                            break;
                            //外键约束异常，不做logger日志
                        }
                        cause = cause.getCause();
                    }
                }
                if (errorMessage == null) {
                    //构建和记录友好和详细的错误信息及消息
                    //生成一个异常流水号，追加到错误消息上显示到前端用户，用户反馈问题时给出此流水号给运维或开发人员快速定位对应具体异常细节
                    String exceptionCode = BaseRuntimeException.buildExceptionCode();
                    errorMessage = exceptionCode + ": 数据删除操作失败，请联系管理员处理！";
                    logger.error(errorMessage, e);
                }
                errorMessageMap.put(entity.getId(), errorMessage);
            }
        }

        int rejectSize = errorMessageMap.size();
        if (rejectSize == 0) {
            return OperationResult.buildSuccessResult("成功删除所有选取记录:" + entities.length + "条");
        } else {
            if (rejectSize == entities.length) {
                return OperationResult.buildFailureResult("所有选取记录删除操作失败", errorMessageMap);
            } else {
                return OperationResult.buildWarningResult("删除操作已处理. 成功:" + (entities.length - rejectSize) + "条" + ",失败:" + rejectSize + "条",
                        errorMessageMap);
            }
        }
    }
}
