package com.entdiy.auth.dao;

import javax.persistence.QueryHint;

import com.entdiy.auth.entity.Department;
import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentDao extends BaseDao<Department, Long> {

    @Query("from Department")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public Iterable<Department> findAllCached();

}
