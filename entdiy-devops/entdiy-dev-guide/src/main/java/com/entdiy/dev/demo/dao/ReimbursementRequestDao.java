package com.entdiy.dev.demo.dao;


import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import com.entdiy.dev.demo.entity.ReimbursementRequest;

@Repository
public interface ReimbursementRequestDao extends BaseDao<ReimbursementRequest, Long> {

}