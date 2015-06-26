package lab.s2jh.aud.service;

import lab.s2jh.aud.dao.JobRunHistDao;
import lab.s2jh.aud.entity.JobRunHist;
import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;

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
