package lab.s2jh.module.sys.dao.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.sys.dao.ConfigPropertyDao;
import lab.s2jh.module.sys.entity.ConfigProperty;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigPropertyDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private ConfigPropertyDao configPropertyDao;

    @Test
    public void save() {
        ConfigProperty entity = MockEntityUtils.buildMockObject(ConfigProperty.class);
        configPropertyDao.save(entity);
        configPropertyDao.findAll();
    }
}
