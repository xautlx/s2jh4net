package xyz.entdiy.shop.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.entdiy.shop.dao.ShopUserDao;
import xyz.entdiy.shop.entity.ShopUser;

@Service
@Transactional
public class ShopUserService extends BaseService<ShopUser, Long> {

    @Autowired
    private ShopUserDao shopUserDao;

    @Override
    protected BaseDao<ShopUser, Long> getEntityDao() {
        return shopUserDao;
    }
}
