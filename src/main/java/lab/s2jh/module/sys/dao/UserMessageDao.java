package lab.s2jh.module.sys.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.UserMessage;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageDao extends BaseDao<UserMessage, Long> {

}