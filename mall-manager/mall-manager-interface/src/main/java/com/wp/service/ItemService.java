package com.wp.service;

import com.wp.common.pojo.PageResult;
import com.wp.pojo.TbItem;

/**
 * @program: WpMall
 * @description: get item by id
 * @author: Pan wu
 * @create: 2018-09-22 11:12
 **/
public interface ItemService {
    TbItem getItemById(Long id);
    PageResult getItemPage(int page, int rows);
}
