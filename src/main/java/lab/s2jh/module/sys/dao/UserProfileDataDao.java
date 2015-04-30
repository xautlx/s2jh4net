package lab.s2jh.module.sys.dao;

import java.util.List;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.sys.entity.UserProfileData;

import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileDataDao extends BaseDao<UserProfileData, Long> {

    UserProfileData findByUserAndCode(User user, String code);

    List<UserProfileData> findByUser(User user);
}