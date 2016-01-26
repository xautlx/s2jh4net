package s2jh.biz.shop.service;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import s2jh.biz.shop.dao.OrderDao;
import s2jh.biz.shop.entity.Order;

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
        entity.setSubmitTime(DateUtils.currentDate());
        return orderDao.save(entity);
    }
    
    public Order payOrder(Order entity) {
        entity.setPayTime(DateUtils.currentDate());
        return orderDao.save(entity);
    }
}
