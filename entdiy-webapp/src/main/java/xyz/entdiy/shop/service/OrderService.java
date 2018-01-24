package xyz.entdiy.shop.service;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.entdiy.shop.dao.OrderDao;
import xyz.entdiy.shop.entity.Order;

@Service
@Transactional
public class OrderService extends BaseService<Order, Long> {

    @Autowired
    private OrderDao orderDao;

    @Override
    protected BaseDao<Order, Long> getEntityDao() {
        return orderDao;
    }

    public Order submitOrder(Order entity) {
        entity.setSubmitTime(DateUtils.currentDateTime());
        return orderDao.save(entity);
    }
    
    public Order payOrder(Order entity) {
        entity.setPayTime(DateUtils.currentDateTime());
        return orderDao.save(entity);
    }
}
