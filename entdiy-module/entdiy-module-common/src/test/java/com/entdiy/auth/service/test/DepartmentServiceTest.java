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
package com.entdiy.auth.service.test;

import com.entdiy.auth.entity.Department;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.test.SpringTransactionalTestCase;
import com.entdiy.core.util.MockEntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class DepartmentServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private DepartmentService departmentService;

    @PostConstruct
    public void init() {
        Department root = MockEntityUtils.buildMockObject(Department.class);
        root.setParent(null);
        root.setName("root");
        departmentService.save(root);

        Department child1 = MockEntityUtils.buildMockObject(Department.class);
        child1.setParent(root);
        child1.setName("child1");
        departmentService.save(child1);

        Department child2 = MockEntityUtils.buildMockObject(Department.class);
        child2.setParent(root);
        child1.setName("child2");
        departmentService.save(child2);
    }

    @Test
    public void findByCodeOrName() {


        logger.debug("LAZY 1. Start...");
        GroupPropertyFilter groupFilter1 = GroupPropertyFilter.buildDefaultAndGroupFilter();
        groupFilter1.forceAnd(new PropertyFilter(PropertyFilter.MatchType.NE, "disabled", true));
        List<Department> datas = departmentService.findByFilters(groupFilter1);
        logger.debug("LAZY 2. Code...");
        datas.forEach((item) -> logger.debug("item code: {}", item.getCode()));
        logger.debug("LAZY 3. enabledChildrenCount...");
        datas.forEach((item) -> logger.debug("item enabledChildrenCount: {}", item.getEnabledChildrenCount()));


        logger.debug("EAGER 1. Start...");
        GroupPropertyFilter groupFilter2 = GroupPropertyFilter.buildDefaultAndGroupFilter();
        groupFilter2.forceAnd(new PropertyFilter(PropertyFilter.MatchType.NE, "disabled", true));
        groupFilter2.forceAnd(new PropertyFilter(PropertyFilter.MatchType.FETCH, "enabledChildrenCount", "INNER"));
        List<Department> datas2 = departmentService.findByFilters(groupFilter2);
        logger.debug("EAGER 2. Code...");
        datas2.forEach((item) -> logger.debug("item code: {}", item.getCode()));
        logger.debug("EAGER 3. enabledChildrenCount...");
        datas2.forEach((item) -> logger.debug("item enabledChildrenCount: {}", item.getEnabledChildrenCount()));
    }
}
