package com.entdiy.schedule.service;


import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.schedule.dao.JobRunHistDao;
import com.entdiy.schedule.entity.JobRunHist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobRunHistService extends BaseService<JobRunHist, Long> {

    @Autowired
    private JobRunHistDao jobRunHistDao;

    @Override
    protected BaseDao<JobRunHist, Long> getEntityDao() {
        return jobRunHistDao;
    }
}
