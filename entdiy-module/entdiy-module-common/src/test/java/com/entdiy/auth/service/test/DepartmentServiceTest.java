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
package com.entdiy.auth.service.test;

import com.entdiy.auth.entity.Department;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.core.test.SpringTransactionalTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DepartmentServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private DepartmentService departmentService;

    @Before
    public void init() {
    }

    @Test
    public void findByCodeOrName() {
        //清空缓存，避免同会话缓存数据干扰
        entityManager.clear();

        Department root = departmentService.findRoot();

        Department d01 = new Department();
        d01.setParent(root);
        d01.setCode("d01");
        d01.setName(d01.getCode());
        departmentService.save(d01);
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        Assert.assertTrue(d01.getLft().equals(2));

        Department d02 = new Department();
        d02.setParent(root);
        d02.setCode("d02");
        d02.setName(d02.getCode());
        departmentService.save(d02);
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();
        Assert.assertTrue(d02.getLft().equals(4));

        Department d0101 = new Department();
        d0101.setParent(d01);
        d0101.setCode("d0101");
        d0101.setName(d0101.getCode());
        departmentService.save(d0101);
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();

        Assert.assertTrue(d0101.getLft().equals(3));

        Department d0102 = new Department();
        d0102.setParent(d01);
        d0102.setCode("d0102");
        d0102.setName(d0102.getCode());
        departmentService.save(d0102);
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();

        Assert.assertTrue(d0102.getLft().equals(5));

        departmentService.delete(departmentService.findByCode("d0101").get());
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();

        Assert.assertTrue(departmentService.findRoot().getRgt().equals(8));


        departmentService.delete(departmentService.findByCode("d0102").get());
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();

        Assert.assertTrue(departmentService.findRoot().getRgt().equals(6));
        Assert.assertTrue(departmentService.findByCode("d02").get().getRgt().equals(5));

        departmentService.delete(departmentService.findByCode("d01").get());
        //清空缓存，避免同会话缓存数据干扰
        entityManager.flush();
        entityManager.clear();
        assertNestedSetModel();

        Assert.assertTrue(departmentService.findRoot().getRgt().equals(4));
        Assert.assertTrue(departmentService.findByCode("d02").get().getRgt().equals(3));
    }

    private void assertNestedSetModel() {
        //清空缓存，避免同会话缓存数据干扰
        entityManager.clear();
        List<Department> entities = entityManager.createQuery("from Department").getResultList();
        entities.forEach(entity -> {
            entityManager.detach(entity);
            logger.debug("id: {}, code: {}, lft: {}, rgt: {}, depth: {}",
                    entity.getId(), entity.getCode(), entity.getLft(), entity.getRgt(), entity.getDepth());
        });
    }
}
