package lab.s2jh.module.sys.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.module.sys.dao.DataDictDao;
import lab.s2jh.module.sys.entity.DataDict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
@Transactional
public class DataDictService extends BaseService<DataDict, Long> {

    private static final Logger logger = LoggerFactory.getLogger(DataDictService.class);

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

    /**
     * 基于主键返回对应的数据字典子集合
     * @param id 主键
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenById(Long id) {
        return findChildrenById(id, false);
    }

    /**
     * 基于主键返回对应的数据字典子集合
     * @param id 主键
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenById(Long id, boolean withFlatChildren) {
        return findChildrens(dataDictDao.findOne(id), withFlatChildren);
    }

    /**
     * 直接基于根节点primaryKey返回对应的数据字典集合
     * @param primaryKey 根节点primaryKey
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenByRootPrimaryKey(String primaryKey) {
        return findChildrenByRootPrimaryKey(primaryKey, false);
    }

    /**
     * 直接基于根节点primaryKey返回对应的数据字典集合
     * @param primaryKey 根节点primaryKey
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<DataDict> findChildrenByRootPrimaryKey(String primaryKey, boolean withFlatChildren) {
        return findChildrens(dataDictDao.findByRootPrimaryKey(primaryKey), withFlatChildren);
    }

    private List<DataDict> findChildrens(DataDict parent, boolean withFlatChildren) {
        if (parent == null) {
            return null;
        }
        List<DataDict> roots = dataDictDao.findEnabledChildrenByParentId(parent.getId());
        if (withFlatChildren) {
            List<DataDict> dataDicts = Lists.newArrayList(roots);
            for (DataDict dataDict : roots) {
                List<DataDict> chidren = dataDictDao.findEnabledChildrenByParentId(dataDict.getId());
                dataDicts.addAll(chidren);
            }
            return dataDicts;
        } else {
            return roots;
        }
    }

    /**
     * 直接基于根节点primaryKey返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     * @param primaryKey 根节点primaryKey
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataByRootPrimaryKey(String primaryKey) {
        return findMapDataByRootPrimaryKey(primaryKey, false);
    }

    /**
     * 基于主键返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     * @param id 主键
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataById(Long id) {
        return findMapDatas(dataDictDao.findOne(id), false);
    }

    /**
     * 直接基于根节点primaryKey返回对应的Map结构key-value数据
     * 注意：如果关联返回子节点，请确保所有节点的primaryKey唯一性，否则出现数据不可预期的覆盖问题
     * @param primaryKey 根节点primaryKey
     * @param withFlatChildren 是否已扁平化结构关联返回子节点数据
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, String> findMapDataByRootPrimaryKey(String primaryKey, boolean withFlatChildren) {
        return findMapDatas(dataDictDao.findByRootPrimaryKey(primaryKey), withFlatChildren);
    }

    private Map<String, String> findMapDatas(DataDict parent, boolean withFlatChildren) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrens(parent, withFlatChildren);
        if (dataDicts != null) {
            for (DataDict dataDict : dataDicts) {
                dataMap.put(dataDict.getPrimaryKey(), dataDict.getPrimaryValue());
            }
        }
        return dataMap;
    }

    @Transactional(readOnly = true)
    public Map<String, DataDict> findMapObjectByRootPrimaryKey(String primaryKey) {
        Map<String, DataDict> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrenByRootPrimaryKey(primaryKey);
        if (dataDicts != null) {
            for (DataDict dataDict : dataDicts) {
                dataMap.put(dataDict.getPrimaryKey(), dataDict);
            }
        } else {
            logger.warn("Undefined DataDict for primaryKey: {}", primaryKey);
        }
        return dataMap;
    }
}
