package lab.s2jh.module.sys.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.NotifyMessage;

import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMessageDao extends BaseDao<NotifyMessage, Long> {

}