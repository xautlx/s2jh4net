package com.entdiy.dev.demo.dao;


import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import com.entdiy.dev.demo.entity.ReimbursementRequestItem;

@Repository
public interface ReimbursementRequestItemDao extends BaseDao<ReimbursementRequestItem, Long> {

}