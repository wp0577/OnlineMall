package com.wp.cart.service;

import com.wp.common.pojo.E3Result;
import com.wp.pojo.TbItem;

import java.util.List;

public interface CartService {
    E3Result addCart(Long itemId, Long userId, int num);

    List<TbItem> getCart(Long id);

    E3Result update(Long itemId, Long userId, int num);

    E3Result delete(Long itemId, Long userId);
}
