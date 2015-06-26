package lab.s2jh.module.schedule.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.schedule.entity.JobBeanCfg;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface JobBeanCfgDao extends BaseDao<JobBeanCfg, Long> {

    @Query("from JobBeanCfg")
    List<JobBeanCfg> findAll();

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    JobBeanCfg findByJobClass(String jobClass);
}