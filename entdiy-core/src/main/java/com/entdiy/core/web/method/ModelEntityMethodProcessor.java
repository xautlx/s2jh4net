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
package com.entdiy.core.web.method;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.util.reflection.ReflectionUtils;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定制对  {@link com.entdiy.core.web.annotation.ModelEntity}  注解参数处理，基于id参数组从数据库查询对象
 *
 * @see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor
 */
public class ModelEntityMethodProcessor implements HandlerMethodArgumentResolver {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(ModelEntity.class));
    }

    /**
     * Resolve the argument from the model or if not found instantiate it with
     * its default if it is available. The model attribute is then populated
     * with request values via data binding and optionally validated
     * if {@code @java.validation.Valid} is present on the argument.
     *
     * @throws BindException if data binding and validation result in an error
     *                       and the next method parameter is not of type {@link Errors}
     * @throws Exception     if WebDataBinder initialization fails
     */
    @Override
    @Nullable
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Assert.state(mavContainer != null, "ModelAttributeMethodProcessor requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "ModelAttributeMethodProcessor requires WebDataBinderFactory");

        MethodParameter nestedParameter = parameter.nestedIfOptional();
        Class<?> clazz = nestedParameter.getNestedParameterType();
        Assert.isTrue(Persistable.class.isAssignableFrom(clazz), "@ModelEntity method parameter should extends from Persistable.class");


        ModelEntity ann = parameter.getParameterAnnotation(ModelEntity.class);
        String name = ann.value();
        mavContainer.setBinding(name, ann.binding());

        Object attribute = null;
        BindingResult bindingResult = null;

        if (mavContainer.containsAttribute(name)) {
            attribute = mavContainer.getModel().get(name);
        } else {
            String id = webRequest.getParameter("id");
            if (StringUtils.isNotBlank(id)) {
                mavContainer.getModel().addAttribute("id", id);
                Class<?> entityIdClass = MethodUtils.getAccessibleMethod(clazz, "getId").getReturnType();

                Object convertedId;
                if (String.class.equals(entityIdClass)) {
                    convertedId = id;
                } else if (Long.class.equals(entityIdClass)) {
                    convertedId = Long.valueOf(id);
                } else if (Integer.class.equals(entityIdClass)) {
                    convertedId = Integer.valueOf(id);
                } else {
                    throw new IllegalStateException("Unsupported entity ID class: " + entityIdClass);
                }

                //查询实体对象
                Object entity = entityManager.find(clazz, convertedId);

                //对后续需要使用的懒加载属性做方法调用，触发关联对象加载，为后续detach做属性数据准备
                if (ann.preFectchLazyFields().length > 0) {
                    for (String prop : ann.preFectchLazyFields()) {
                        try {
                            Object propValue = MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(prop));
                            //Hibernate.initialize(propValue);
                            if (propValue != null && propValue instanceof Collection<?>) {
                                ((Collection<?>) propValue).size();
                            } else if (propValue != null && propValue instanceof Persistable<?>) {
                                ((Persistable<?>) propValue).getId();
                            }
                        } catch (Exception e) {
                            throw new ServiceException("error.init.detached.entity", e);
                        }
                    }
                }

                //转换为游离态，用于后续数据绑定
                entityManager.detach(entity);

                attribute = entity;
            } else {
                attribute = BeanUtils.instantiateClass(clazz);

                //对Embedded嵌入对象属性初始化空对象
                List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, Embedded.class);
                for (Field field : fields) {
                    ReflectionUtils.invokeSetterMethod(attribute, field.getName(), BeanUtils.instantiateClass(field.getType()));
                }
            }

            //对于GET类型请求，追加校验规则JSON字符串属性
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod()) && !GlobalConstant.NONE_VALUE.equals(ann.validateRules())) {
                mavContainer.getModel().addAttribute(ann.validateRules(), ServletUtils.buildValidateRules(clazz));
            }
        }

        if (bindingResult == null) {
            // Bean property binding and validation;
            // skipped in case of binding failure on construction.
            WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);

            //传入绑定控制属性
            binder.setAllowedFields(ann.allowedFields());
            binder.setDisallowedFields(ann.disallowedFields());

            if (binder.getTarget() != null) {
                if (!mavContainer.isBindingDisabled(name)) {
                    bindRequestParameters(binder, webRequest);
                }
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
            // Value type adaptation, also covering java.util.Optional
            if (!parameter.getParameterType().isInstance(attribute)) {
                attribute = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
            }
            bindingResult = binder.getBindingResult();
        }

        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = bindingResult.getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return attribute;
    }


    /**
     * This implementation downcasts {@link WebDataBinder} to
     * {@link ServletRequestDataBinder} before binding.
     */
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }

    /**
     * Validate the model attribute if applicable.
     * <p>The default implementation checks for {@code @javax.validation.Valid},
     * Spring's {@link org.springframework.validation.annotation.Validated},
     * and custom annotations whose name starts with "Valid".
     *
     * @param binder    the DataBinder to be used
     * @param parameter the method parameter declaration
     */
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                binder.validate(validationHints);
                break;
            }
        }
    }

    /**
     * Whether to raise a fatal bind exception on validation errors.
     * <p>The default implementation delegates to {@link #isBindExceptionRequired(MethodParameter)}.
     *
     * @param binder    the data binder used to perform data binding
     * @param parameter the method parameter declaration
     * @return {@code true} if the next method parameter is not of type {@link Errors}
     * @see #isBindExceptionRequired(MethodParameter)
     */
    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        return isBindExceptionRequired(parameter);
    }

    /**
     * Whether to raise a fatal bind exception on validation errors.
     *
     * @param parameter the method parameter declaration
     * @return {@code true} if the next method parameter is not of type {@link Errors}
     * @since 5.0
     */
    protected boolean isBindExceptionRequired(MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }


}
