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
package com.entdiy.auth.service;

import java.util.Collections;
import java.util.List;

import com.entdiy.auth.dao.DepartmentDao;
import com.entdiy.auth.entity.Department;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class DepartmentService extends BaseService<Department, Long> {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    protected BaseDao<Department, Long> getEntityDao() {
        return departmentDao;
    }

    @Transactional(readOnly = true)
    public List<Department> findRoots() {
        List<Department> roots = Lists.newArrayList();
        Iterable<Department> items = departmentDao.findAllCached();
        for (Department item : items) {
            if (item.getParent() == null) {
                roots.add(item);
            }
        }
        Collections.sort(roots);
        return roots;
    }

    @Transactional(readOnly = true)
    public List<Department> findChildren(Department parent) {
        if (parent == null) {
            return findRoots();
        }
        List<Department> children = Lists.newArrayList();
        Iterable<Department> items = departmentDao.findAllCached();
        for (Department item : items) {
            if (parent.equals(item.getParent())) {
                children.add(item);
            }
        }
        Collections.sort(children);
        return children;
    }
}
