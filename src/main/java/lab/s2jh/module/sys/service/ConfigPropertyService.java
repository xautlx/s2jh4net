package lab.s2jh.module.sys.service;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.sys.dao.ConfigPropertyDao;
import lab.s2jh.module.sys.entity.ConfigProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigPropertyService extends BaseService<ConfigProperty, Long> {

    @Autowired
    private ConfigPropertyDao configPropertyDao;

    @Override
    protected BaseDao<ConfigProperty, Long> getEntityDao() {
        return configPropertyDao;
    }

    @Transactional(readOnly = true)
    public ConfigProperty findByPropKey(String propKey) {
        return configPropertyDao.findByPropKey(propKey);
    }
}
