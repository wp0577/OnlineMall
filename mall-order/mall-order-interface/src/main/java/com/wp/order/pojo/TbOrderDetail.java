package com.wp.order.pojo;

import com.wp.pojo.TbOrder;
import com.wp.pojo.TbOrderItem;
import com.wp.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

public class TbOrderDetail extends TbOrder implements Serializable {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
