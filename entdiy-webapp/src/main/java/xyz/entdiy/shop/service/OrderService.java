/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
