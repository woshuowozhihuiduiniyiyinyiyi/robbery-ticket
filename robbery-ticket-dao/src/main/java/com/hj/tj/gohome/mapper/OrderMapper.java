package com.hj.tj.gohome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.tj.gohome.entity.Order;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    Integer getTotalProfit(List<Integer> statusList);

}
