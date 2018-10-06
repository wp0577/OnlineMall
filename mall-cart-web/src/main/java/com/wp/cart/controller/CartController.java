package com.wp.cart.controller;

import com.wp.cart.service.CartService;
import com.wp.common.pojo.E3Result;
import com.wp.common.util.CookieUtils;
import com.wp.common.util.JsonUtils;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbUser;
import com.wp.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Value("${CART_COOKIE}")
    private String CART_COOKIE;
    @Value("${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartServicel;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
                          HttpServletResponse response) {
        //判断是否登录
        //如果登录则加入redis
        Object user = request.getAttribute("user");
        if(user != null) {
            E3Result e3Result = cartServicel.addCart(itemId, ((TbUser) user).getId(), num);
            return "cartSuccess";
        }
        //从cookie中查询商品列表。
        List<TbItem> cartList = getCartList(request);
        //设置一个flag，用来表示是否存在商品'
        boolean flag = false;
        for (TbItem t:cartList) {
            //判断商品在商品列表中是否存在。
            if(t.getId().equals(itemId)) {
                //如果存在，商品数量相加。
                t.setNum(t.getNum() + num);
                flag = true;
                break;
            }
        }
        if(!flag) {
            //不存在，根据商品id查询商品信息。
            TbItem itemById = itemService.getItemById(itemId);
            itemById.setNum(num);
            //如果有图片，则取第一张
            if(StringUtils.isNotBlank(itemById.getImage())) {
                itemById.setImage(itemById.getImage().split(",")[0]);
            }
            //把商品添加到购车列表。
            cartList.add(itemById);
        }
        //把购车商品列表写入cookie。
        CookieUtils.setCookie(request, response ,CART_COOKIE, JsonUtils.objectToJson(cartList), CART_COOKIE_EXPIRE, true);
        return "cartSuccess";
    }

    /** 
    * @Description: 该业务逻辑是，登录和未登录的购物车是不同的数据，不会合并 
    * @Param:  
    * @return:  
    * @Author: Pan wu
    * @Date: 10/5/18 
    */ 
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request) {
        Object user = request.getAttribute("user");
        if(user != null) {
            List<TbItem> tbItems = cartServicel.getCart(((TbUser) user).getId());
            request.setAttribute("cartList", tbItems);
            return "cart";
        }
        List<TbItem> cartList = getCartList(request);
        request.setAttribute("cartList", cartList);
        return "cart";
    }

    @RequestMapping("cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCart(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        Object user = request.getAttribute("user");
        if(user != null) {
            return cartServicel.update(itemId, ((TbUser) user).getId(), num);
        }
        List<TbItem> cartList = getCartList(request);
        for (TbItem tbItem:cartList) {
            if(tbItem.getId().equals(itemId)) {
                tbItem.setNum(num);
                break;
            }
        }
        CookieUtils.setCookie(request, response ,CART_COOKIE, JsonUtils.objectToJson(cartList), CART_COOKIE_EXPIRE, true);
        return E3Result.ok();
    }


    @RequestMapping("cart/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        Object user = request.getAttribute("user");
        if(user != null) {
            cartServicel.delete(itemId, ((TbUser) user).getId());
            return "redirect:/cart/cart.html";
        }
        List<TbItem> cartList = getCartList(request);
        for (TbItem tbItem:cartList) {
            if(tbItem.getId().equals(itemId)) {
                cartList.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request, response ,CART_COOKIE, JsonUtils.objectToJson(cartList), CART_COOKIE_EXPIRE, true);
        //需要在后缀加上.html，不然不会被springMVC拦截
        return "redirect:/cart/cart.html";
    }

    /** 
    * @Description: get cart list from cookie, if not exisit return new Arraylist(); 
    * @Param:  
    * @return:  
    * @Author: Pan wu
    * @Date: 10/5/18 
    */ 
    private List<TbItem> getCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, CART_COOKIE, true);
        if(StringUtils.isNotBlank(cookieValue)) {
            List<TbItem> tbItems = JsonUtils.jsonToList(cookieValue, TbItem.class);
            return tbItems;
        }
        else return new ArrayList<>();
    }

}
