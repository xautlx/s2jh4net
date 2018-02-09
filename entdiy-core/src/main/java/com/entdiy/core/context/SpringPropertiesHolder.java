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
package com.entdiy.core.context;

import com.entdiy.core.web.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;

/**
 * 扩展标准的PropertyPlaceholderConfigurer把属性文件中的配置参数信息放入全局Map变量便于其他接口访问key-value配置数据
 */
public class SpringPropertiesHolder extends PropertySourcesPlaceholderConfigurer {

	private static Logger logger = LoggerFactory.getLogger(SpringPropertiesHolder.class);

	private static ConfigurablePropertyResolver propertyResolver;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			final ConfigurablePropertyResolver propertyResolver) throws BeansException {
		super.processProperties(beanFactoryToProcess, propertyResolver);
		logger.info("Setting propertyResolver to SpringPropertiesHolder");
		SpringPropertiesHolder.propertyResolver = propertyResolver;

		//尽早初始化全局参数值，以便后续逻辑使用
		AppContextHolder.setBuildVersion(propertyResolver.getProperty("build.version"));
		AppContextHolder.setDemoMode(propertyResolver.getProperty("demo.mode"));
		AppContextHolder.setDevMode(propertyResolver.getProperty("dev.mode"));
		AppContextHolder.setSystemName(propertyResolver.getProperty("system.name"));
	}

	public static String getProperty(String name) {
		return propertyResolver.getProperty(name);
	}
}
