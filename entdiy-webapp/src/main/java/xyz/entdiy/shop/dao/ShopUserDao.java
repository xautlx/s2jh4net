package xyz.entdiy.shop.dao;

import com.entdiy.core.dao.jpa.BaseDao;
import org.springframework.stereotype.Repository;
import xyz.entdiy.shop.entity.ShopUser;

@Repository
public interface ShopUserDao extends BaseDao<ShopUser, Long> {

}
