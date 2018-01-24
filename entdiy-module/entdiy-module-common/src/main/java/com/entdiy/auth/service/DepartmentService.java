/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.auth.service;

import com.entdiy.auth.dao.DepartmentDao;
import com.entdiy.auth.entity.Department;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentService extends BaseService<Department, Long> {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    protected BaseDao<Department, Long> getEntityDao() {
        return departmentDao;
    }

//    public List<Department.DepartmentTreeDataDto> findTreeDatas(String keyword) {
//        String hql = "SELECT d.id as id, d.code as code, d.name as name, " +
//                "(select count(*) from Department dd where dd.parent=d.id and dd.disabled=false) as childrenCount " +
//                "FROM Department d where disabled=false ";
//
//        if (StringUtils.isBlank(keyword)) {
//            return departmentDao.findTreeDataRoots();
//        } else {
//            return departmentDao.findTreeDataByKeyword(keyword);
//        }
//    }
}
