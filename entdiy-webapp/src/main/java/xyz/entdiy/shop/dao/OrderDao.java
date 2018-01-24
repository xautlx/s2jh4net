package xyz.entdiy.shop.dao;

import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import xyz.entdiy.shop.entity.Order;

@Repository
public interface OrderDao extends BaseDao<Order, Long> {

}
