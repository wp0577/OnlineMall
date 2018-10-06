package com.wp.cart.controller;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.CookieUtils;
import com.wp.common.util.JsonUtils;
import com.wp.pojo.TbItem;
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

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
                          HttpServletResponse response) {
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

    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request) {
        List<TbItem> cartList = getCartList(request);
        request.setAttribute("cartList", cartList);
        return "cart";
    }

    @RequestMapping("cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCart(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
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
