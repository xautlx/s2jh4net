package com.entdiy.sys.service;

import java.util.List;
import java.util.Map;

import com.entdiy.auth.entity.User;
import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.UserProfileDataDao;
import com.entdiy.sys.entity.UserProfileData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class UserProfileDataService extends BaseService<UserProfileData, Long> {

    @Autowired
    private UserProfileDataDao userProfileDataDao;

    @Override
    protected BaseDao<UserProfileData, Long> getEntityDao() {
        return userProfileDataDao;
    }

    @Transactional(readOnly = true)
    public UserProfileData findByUserAndCode(User user, String code) {
        return userProfileDataDao.findByUserAndCode(user, code);
    }

    @Transactional(readOnly = true)
    public List<UserProfileData> findByUser(User user) {
        return userProfileDataDao.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findMapDataByUser(User user) {
        Map<String, Object> mapData = Maps.newHashMap();
        List<UserProfileData> datas = userProfileDataDao.findByUser(user);
        for (UserProfileData data : datas) {
            mapData.put(data.getCode(), data.getValue());
        }
        return mapData;
    }
}
