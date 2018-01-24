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
package com.entdiy.aud.aop;

import com.entdiy.aud.envers.ExtRevisionListener;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.web.AppContextHolder;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * 基于Spring AOP对Controller方法调用进行拦截处理
 * 提取相关的调用信息设置到 @see ExtRevisionListener 传递给Hibernate Envers组件记录
 */
public class ControllerInvokeAuditor {

    private final static Logger logger = LoggerFactory.getLogger(ControllerInvokeAuditor.class);

    private static Map<String, Map<String, String>> cachedMethodDatas = Maps.newHashMap();

    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            String key = method.toString();

            if (logger.isDebugEnabled()) {
                String monthName = method.getName();
                String className = joinPoint.getThis().getClass().getName();
                if (className.indexOf("$$") > -1) { // 如果是CGLIB动态生成的类  
                    className = StringUtils.substringBefore(className, "$$");
                }
                logger.debug("AOP Aspect: {}, Point: {}.{}", ControllerInvokeAuditor.class, className, monthName);
            }

            Map<String, String> cachedMethodData = cachedMethodDatas.get(key);
            //如果已有方法缓存数据，则直接处理
            if (cachedMethodData != null && !AppContextHolder.isDevMode()) {
                logger.debug("Controller method audit, cached data: {}", cachedMethodData);
                ExtRevisionListener.setKeyValue(cachedMethodData);
            } else {
                //如果没有缓存数据，则判断处理
                RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                RequestMethod[] requestMethods = methodRequestMapping.method();
                for (RequestMethod requestMethod : requestMethods) {
                    //只处理POST方法
                    if (RequestMethod.POST.equals(requestMethod)) {

                        //构造缓存数据对象，并计算相关属性放到缓存对象
                        cachedMethodData = Maps.newHashMap();

                        Class<?> thisClazz = joinPoint.getThis().getClass();
                        String className = thisClazz.getName();
                        if (className.indexOf("$$") > -1) { // 如果是CGLIB动态生成的类  
                            className = StringUtils.substringBefore(className, "$$");
                        }
                        Class<?> controllerClazz = ClassUtils.forName(className);
                        String requestMappingUri = "";
                        RequestMapping classRequestMapping = controllerClazz.getAnnotation(RequestMapping.class);
                        if (classRequestMapping != null) {
                            requestMappingUri += StringUtils.join(classRequestMapping.value());
                        }
                        requestMappingUri += StringUtils.join(methodRequestMapping.value());
                        cachedMethodData.put(ExtRevisionListener.requestMappingUri, requestMappingUri);

                        cachedMethodData.put(ExtRevisionListener.controllerClassName, className);
                        MetaData clazzMetaData = controllerClazz.getAnnotation(MetaData.class);
                        if (clazzMetaData != null) {
                            cachedMethodData.put(ExtRevisionListener.controllerClassLabel, clazzMetaData.value());
                        }

                        Object genericClz = controllerClazz.getGenericSuperclass();
                        if (genericClz instanceof ParameterizedType) {
                            Class<?> entityClass = (Class<?>) ((ParameterizedType) genericClz).getActualTypeArguments()[0];
                            cachedMethodData.put(ExtRevisionListener.entityClassName, entityClass.getName());
                        }

                        cachedMethodData.put(ExtRevisionListener.controllerMethodName, method.getName());
                        cachedMethodData.put(ExtRevisionListener.controllerMethodType, requestMethod.name());

                        MetaData methodMetaData = method.getAnnotation(MetaData.class);
                        if (methodMetaData != null) {
                            cachedMethodData.put(ExtRevisionListener.controllerMethodLabel, methodMetaData.value());
                        }

                        cachedMethodDatas.put(key, cachedMethodData);

                        logger.debug("Controller method audit, init data: {}", cachedMethodData);
                        ExtRevisionListener.setKeyValue(cachedMethodData);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            //捕获审计信息处理异常，避免影响正常的业务流程
            logger.error(e.getMessage(), e);
        }
        return joinPoint.proceed();
    }
}
