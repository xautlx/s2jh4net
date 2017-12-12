package com.entdiy.dev.demo.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.dev.demo.entity.ReimbursementRequest;
import com.entdiy.dev.demo.dao.ReimbursementRequestDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReimbursementRequestService extends BaseService<ReimbursementRequest,Long>{
    
    @Autowired
    private ReimbursementRequestDao reimbursementRequestDao;

    @Override
    protected BaseDao<ReimbursementRequest, Long> getEntityDao() {
        return reimbursementRequestDao;
    }
}
