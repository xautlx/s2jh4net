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
package com.entdiy.auth.dao.test;

import com.entdiy.auth.dao.DepartmentDao;
import com.entdiy.auth.entity.Department;
import com.entdiy.core.test.SpringTransactionalTestCase;
import com.entdiy.core.util.MockEntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class DepartmentDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private DepartmentDao departmentDao;

    @PostConstruct
    public void init() {
        Department root = MockEntityUtils.buildMockObject(Department.class);
        root.setParent(null);
        root.setName("root");
        departmentDao.save(root);

        Department child1 = MockEntityUtils.buildMockObject(Department.class);
        child1.setParent(root);
        child1.setName("child1");
        departmentDao.save(child1);

        Department child2 = MockEntityUtils.buildMockObject(Department.class);
        child2.setParent(root);
        child1.setName("child2");
        departmentDao.save(child2);
    }

    @Test
    public void findAll() {
        logger.debug("1. Start...");
        List<Department> datas = departmentDao.findAll();
        logger.debug("2. Code...");
        datas.forEach((item) -> logger.debug("item code: {}", item.getCode()));
    }
}
