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
