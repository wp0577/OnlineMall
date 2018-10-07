package com.wp.order.controller;

import com.wp.cart.service.CartService;
import com.wp.common.pojo.E3Result;
import com.wp.order.pojo.TbOrderDetail;
import com.wp.order.service.OrderService;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request) {
        //get info of user, there must exist user because interceptor
        TbUser user = (TbUser) request.getAttribute("user");
        //get cart from redis by CartService and userId
        List<TbItem> cart = cartService.getCart(user.getId());
        request.setAttribute("cartList", cart);
        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String success(TbOrderDetail tbOrderDetail, HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("user");
        tbOrderDetail.setUserId(user.getId());
        tbOrderDetail.setBuyerNick(user.getUsername());
        E3Result order = orderService.createOrder(tbOrderDetail);
        request.setAttribute("orderId", order.getData());
        request.setAttribute("payment", tbOrderDetail.getPayment());

        return "success";
    }

}
