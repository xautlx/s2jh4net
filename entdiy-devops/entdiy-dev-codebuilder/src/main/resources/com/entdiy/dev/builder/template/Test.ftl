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
package ${root_package}.service.test;

import java.util.List;

import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;
import com.entdiy.core.test.SpringTransactionalTestCase;
import com.entdiy.core.util.MockEntityUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Sets;

public class ${entity_name}ServiceTest extends SpringTransactionalTestCase {

	@Autowired
	private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @Test
    public void findByPage() {
        //Insert mock entity
        ${entity_name} entity = MockEntityUtils.buildMockObject(${entity_name}.class);
        ${entity_name_uncapitalize}Service.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<${entity_name}> items = ${entity_name_uncapitalize}Service.findAll(entity.getId());
        Assert.assertTrue(items.size() >= 1);
    }
}