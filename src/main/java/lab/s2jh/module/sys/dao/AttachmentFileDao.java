package lab.s2jh.module.sys.dao;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.AttachmentFile;

import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentFileDao extends BaseDao<AttachmentFile, String> {

}
