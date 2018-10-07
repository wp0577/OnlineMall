package com.wp.order.service;

import com.wp.common.pojo.E3Result;
import com.wp.order.pojo.TbOrderDetail;

public interface OrderService {
    E3Result createOrder(TbOrderDetail tbOrderDetail);
}
