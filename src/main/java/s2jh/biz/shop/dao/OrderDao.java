package s2jh.biz.shop.dao;

import lab.s2jh.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import s2jh.biz.shop.entity.Order;

@Repository
public interface OrderDao extends BaseDao<Order, Long> {

}
