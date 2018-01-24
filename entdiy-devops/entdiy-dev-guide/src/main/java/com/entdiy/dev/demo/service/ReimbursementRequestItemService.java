package com.entdiy.dev.demo.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.dev.demo.entity.ReimbursementRequestItem;
import com.entdiy.dev.demo.dao.ReimbursementRequestItemDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReimbursementRequestItemService extends BaseService<ReimbursementRequestItem,Long>{
    
    @Autowired
    private ReimbursementRequestItemDao reimbursementRequestItemDao;

    @Override
    protected BaseDao<ReimbursementRequestItem, Long> getEntityDao() {
        return reimbursementRequestItemDao;
    }
}
