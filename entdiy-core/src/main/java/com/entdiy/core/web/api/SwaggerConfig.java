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
package com.entdiy.core.web.api;

import com.entdiy.core.web.AppContextHolder;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = Lists.newArrayList();
        ParameterBuilder sign = new ParameterBuilder();
        sign.name("sign").description("客户端鉴权签名参数").modelRef(new ModelRef("string")).parameterType("header").required(true).defaultValue("dev");
        pars.add(sign.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(AppContextHolder.getSystemName() + " Restful API 接口列表")
                .description("Powered by https://www.EntDIY.com")
                .version(AppContextHolder.getBuildVersion())
                .contact(new Contact("EntDIY", "https://www.EntDIY.com", "xautlx@hotmail.com"))
                .termsOfServiceUrl("https://www.EntDIY.com")
                .build();
    }
}