package lab.s2jh.module.schedule.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.schedule.entity.JobRunHist;

import org.springframework.stereotype.Repository;

@Repository
public interface JobRunHistDao extends BaseDao<JobRunHist, Long> {

}