package com.wp.order.service.imp;

import com.wp.common.jedis.JedisClient;
import com.wp.common.pojo.E3Result;
import com.wp.mapper.TbOrderItemMapper;
import com.wp.mapper.TbOrderMapper;
import com.wp.mapper.TbOrderShippingMapper;
import com.wp.order.pojo.TbOrderDetail;
import com.wp.order.service.OrderService;
import com.wp.pojo.TbOrderItem;
import com.wp.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN_KEY}")
    private String ORDER_ID_BEGIN_KEY;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public E3Result createOrder(TbOrderDetail tbOrderDetail) {
        //接收表单的数据
        //生成订单id
        if (!jedisClient.exists(ORDER_GEN_KEY)) {
            jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN_KEY);
        }
        //订单id用redis的incr函数生成
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        //向订单表插入数据。
        tbOrderDetail.setOrderId(orderId);
        tbOrderDetail.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        tbOrderDetail.setStatus(1);
        tbOrderDetail.setCreateTime(new Date());
        tbOrderDetail.setUpdateTime(new Date());
        // 3、向订单表插入数据。
        tbOrderMapper.insert(tbOrderDetail);
        //向订单明细表插入数据
        List<TbOrderItem> orderItems = tbOrderDetail.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId);
            //插入数据
            tbOrderItemMapper.insert(tbOrderItem);
        }
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = tbOrderDetail.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insert(orderShipping);
        // 6、返回e3Result。
        return E3Result.ok(orderId);

    }
}
