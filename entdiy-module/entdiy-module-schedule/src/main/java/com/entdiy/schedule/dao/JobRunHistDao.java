package com.entdiy.schedule.dao;


import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.schedule.entity.JobRunHist;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRunHistDao extends BaseDao<JobRunHist, Long> {

}