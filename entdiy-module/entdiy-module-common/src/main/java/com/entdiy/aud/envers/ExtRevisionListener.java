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
package com.entdiy.aud.envers;

import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.security.AuthContextHolder;
import com.google.common.collect.Maps;
import org.hibernate.envers.RevisionListener;

import java.util.Map;

/**
 * 扩展默认的RevisionListener，额外追加记录登录用户信息
 *
 * @see "http://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html/ch15.html"
 */
public class ExtRevisionListener implements RevisionListener {

    /**
     * 以ThreadLocal机制把Web层相关审计属性值带入Envers监听器
     */
    private static final ThreadLocal<Map<String, String>> operationDataContainer = new ThreadLocal();

    public final static String entityClassName = "entityClassName";
    public final static String controllerClassName = "controllerClassName";
    public final static String controllerClassLabel = "controllerClassLabel";
    public final static String controllerMethodName = "controllerMethodName";
    public final static String controllerMethodLabel = "controllerMethodLabel";
    public final static String controllerMethodType = "controllerMethodType";
    public final static String requestMappingUri = "requestMappingUri";

    public static void setKeyValue(Map<String, String> datas) {
        Map<String, String> operationData = operationDataContainer.get();
        if (operationData == null) {
            operationData = Maps.newHashMap();
            operationDataContainer.set(operationData);
        }
        operationData.putAll(datas);
    }

    @Override
    public void newRevision(Object revisionEntity) {
        ExtDefaultRevisionEntity revEntity = (ExtDefaultRevisionEntity) revisionEntity;
        AuthUserDetails authUserDetails = AuthContextHolder.getDefaultAuthUserDetails();
        if (authUserDetails != null) {
            revEntity.setOperationAccountId(authUserDetails.getAccountId());
            revEntity.setOperationUserName(authUserDetails.getUsername());
            revEntity.setOperationDataDomain(authUserDetails.getDataDomain());
        }
        Map<String, String> operationData = operationDataContainer.get();
        //获取后立即清空线程容器，避免多线程环境干扰
        operationDataContainer.remove();
        if (operationData != null) {
            revEntity.setControllerClassName(operationData.get(controllerClassName));
            revEntity.setControllerClassLabel(operationData.get(controllerClassLabel));
            revEntity.setControllerMethodName(operationData.get(controllerMethodName));
            revEntity.setControllerMethodLabel(operationData.get(controllerMethodLabel));
            revEntity.setControllerMethodType(operationData.get(controllerMethodType));
            revEntity.setRequestMappingUri(operationData.get(requestMappingUri));
            revEntity.setEntityClassName(operationData.get(entityClassName));
        }
    }
}
