package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.order.OrderSaveParam;

public interface OrderService {

    /**
     * 新增或编辑订单
     *
     * @param orderSaveParam
     * @return
     */
    Integer saveOrder(OrderSaveParam orderSaveParam);
}
