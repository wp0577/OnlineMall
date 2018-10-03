package com.wp.service;

import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemDesc;

/**
 * @program: WpMall
 * @description: get item by id
 * @author: Pan wu
 * @create: 2018-09-22 11:12
 **/
public interface ItemService {

    TbItem getItemById(Long id);

    PageResult getItemPage(int page, int rows);

    E3Result saveItem(TbItem tbItem, String desc);

    TbItemDesc getItemDescById(long id);

    void updateItem(TbItem tbItem, String desc);

    void deleteItemById(long[] ids);

    void updateItemByStatus(long[] ids, int i);
}
