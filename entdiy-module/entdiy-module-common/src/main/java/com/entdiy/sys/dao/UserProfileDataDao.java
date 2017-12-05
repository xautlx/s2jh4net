package com.entdiy.sys.dao;

import java.util.List;

import com.entdiy.auth.entity.User;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.UserProfileData;

import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileDataDao extends BaseDao<UserProfileData, Long> {

    UserProfileData findByUserAndCode(User user, String code);

    List<UserProfileData> findByUser(User user);
}