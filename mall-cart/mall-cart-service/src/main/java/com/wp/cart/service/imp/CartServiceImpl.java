package com.wp.cart.service.imp;

import com.wp.cart.service.CartService;
import com.wp.common.jedis.JedisClient;
import com.wp.common.pojo.E3Result;
import com.wp.common.util.JsonUtils;
import com.wp.mapper.TbItemMapper;
import com.wp.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemMapper tbItemMapper;

    /**
    * @Description: cart info will store in redis includes userInfo (CART_REDIS_KEY:userId, itemId, value)
    * @Param:  Long itemId, Long userId, int num
    * @return:  E3Result
    * @Author: Pan wu
    * @Date: 10/5/18
    */
    @Override
    public E3Result addCart(Long itemId, Long userId, int num) {
        //根据商品id查询商品信息
        //把商品信息保存到redis
        //	从redis中判断购物车中是否有此商品
        Boolean exists = jedisClient.hexists(CART_REDIS_KEY+":"+userId, itemId+"");
        //	如果有，数量相加
        if(exists) {
            String hget = jedisClient.hget(CART_REDIS_KEY + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
            jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //	如果没有，根据商品id查询商品信息。
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        if(StringUtils.isNotBlank(tbItem.getImage())) {
            tbItem.setImage(tbItem.getImage().split(",")[0]);
        }
        //	把商品信息添加到购物车
        jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        //返回值。E3Result
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCart(Long userId) {
        List<String> hvals = jedisClient.hvals(CART_REDIS_KEY + ":" + userId);
        List<TbItem> tbItems = new ArrayList<>();
        for (String s:hvals) {
            tbItems.add(JsonUtils.jsonToPojo(s, TbItem.class));
        }
        return tbItems;
    }

    @Override
    public E3Result update(Long itemId, Long userId, int num) {
        String hget = jedisClient.hget(CART_REDIS_KEY + ":" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
        tbItem.setNum(num);
        jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result delete(Long itemId, Long userId) {
        jedisClient.hdel(CART_REDIS_KEY + ":" + userId, itemId + "");
        return E3Result.ok();
    }
}
