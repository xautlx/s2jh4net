package com.entdiy.sys.service;

import java.util.List;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.ConfigPropertyDao;
import com.entdiy.sys.entity.ConfigProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigPropertyService extends BaseService<ConfigProperty, Long> {

    @Autowired
    private ConfigPropertyDao configPropertyDao;

    public List<ConfigProperty> findAllCached() {
        return configPropertyDao.findAllCached();
    }

    @Override
    protected BaseDao<ConfigProperty, Long> getEntityDao() {
        return configPropertyDao;
    }

    @Transactional(readOnly = true)
    public ConfigProperty findByPropKey(String propKey) {
        return configPropertyDao.findByPropKey(propKey);
    }

    @Transactional(readOnly = true)
    public String findValueByPropKey(String propKey) {
        ConfigProperty configProperty = configPropertyDao.findByPropKey(propKey);
        if (configProperty != null) {
            return configProperty.getSimpleValue();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public String findHtmlByPropKey(String propKey) {
        ConfigProperty configProperty = configPropertyDao.findByPropKey(propKey);
        if (configProperty != null) {
            return configProperty.getHtmlValue();
        }
        return null;
    }
}
