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
package com.entdiy.support.web.method;

import com.entdiy.auth.entity.Account;
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.security.annotation.AuthAccount;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @see AuthAccount
 * @see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor
 */
public class AuthAccountMethodProcessor implements HandlerMethodArgumentResolver {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(AuthAccount.class));
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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthAccount ann = parameter.getParameterAnnotation(AuthAccount.class);

        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (ann.required() && authUserDetails == null) {
            throw new UnauthenticatedException();
        }
        if (authUserDetails == null) {
            return null;
        }
        Account account = entityManager.find(Account.class, authUserDetails.getAccountId());
        if (ann.required() && account == null) {
            throw new UnauthenticatedException();
        }
        if (account != null) {
            entityManager.detach(account);
        }
        return account;
    }
}
