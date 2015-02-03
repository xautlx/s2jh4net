package lab.s2jh.module.schedule.service;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.schedule.dao.JobRunHistDao;
import lab.s2jh.module.schedule.entity.JobRunHist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    /**
     * 异步新开事务写入定时任务运行记录
     * @param entity
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithAsyncAndNewTransition(JobRunHist entity) {
        jobRunHistDao.save(entity);
    }
}
