package lab.s2jh.module.sys.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.sys.dao.DataDictDao;
import lab.s2jh.module.sys.entity.DataDict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class DataDictService extends BaseService<DataDict, Long> {

    @Autowired
    private DataDictDao dataDictDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected BaseDao<DataDict, Long> getEntityDao() {
        return dataDictDao;
    }

    @Transactional(readOnly = true)
    public List<DataDict> findAllCached() {
        return dataDictDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public List<DataDict> findChildrenByPrimaryKey(String primaryKey) {
        DataDict parent = dataDictDao.findByPrimaryKey(primaryKey);
        return dataDictDao.findChildrenByParentAndDisabled(parent, false);
    }

    @Transactional(readOnly = true)
    public Map<String, String> findMapDataByPrimaryKey(String primaryKey) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrenByPrimaryKey(primaryKey);
        for (DataDict dataDict : dataDicts) {
            dataMap.put(dataDict.getPrimaryKey(), dataDict.getPrimaryValue());
        }
        return dataMap;
    }

    @Transactional(readOnly = true)
    public Map<String, DataDict> findMapObjectByPrimaryKey(String primaryKey) {
        Map<String, DataDict> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrenByPrimaryKey(primaryKey);
        for (DataDict dataDict : dataDicts) {
            dataMap.put(dataDict.getPrimaryKey(), dataDict);
        }
        return dataMap;
    }
}
