package com.wp.controller;

import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemDesc;
import com.wp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: WpMall
 * @description: item controller
 * @author: Pan wu
 * @create: 2018-09-22 11:15
 **/
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    /*  使用RESTful风格开发的接口，根据id查询商品，接口地址是：
        http://127.0.0.1/item/1
        我们需要从url上获取商品id，步骤如下：
        1. 使用注解@RequestMapping("item/{id}")声明请求的url
        {xxx}叫做占位符，请求的URL可以是“item /1”或“item/2”
        2. 使用(@PathVariable() Integer id)获取url上的数据*/
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem itemById = itemService.getItemById(itemId);
        return itemById;
    }

    @RequestMapping("item/list")
    @ResponseBody
    public PageResult itemList(int page, int rows) {
        PageResult itemPage = itemService.getItemPage(page, rows);
        return itemPage;
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public E3Result itemSave(TbItem tbItem, String desc) {
        E3Result e3Result = itemService.saveItem(tbItem, desc);
        return e3Result;
    }

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public E3Result itemUpdate(TbItem tbItem, String desc) {
        itemService.updateItem(tbItem, desc);
        return E3Result.ok();
    }

    /**
    * @Description: return item desc as json type
    * @Param: item id
    * @return:  json data
    * @Author: Pan wu
    * @Date: 9/25/18
    */
    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public E3Result getItemDescById(@PathVariable long id) {
        TbItemDesc desc = itemService.getItemDescById(id);
        if(desc!=null) {
            return E3Result.ok(desc);
        }
        else
        return E3Result.ok();
    }

    /**
    * @Description: delete one or more items
    * @Param:  ids
    * @return:
    * @Author: Pan wu
    * @Date: 9/25/18
    */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result deleteItemById(long[] ids){
        itemService.deleteItemById(ids);
        return E3Result.ok();
    }

    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public E3Result reshelf(long[] ids) {
        itemService.updateItemByStatus(ids, 2);
        return E3Result.ok();
    }

    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public E3Result instock(long[] ids) {
        itemService.updateItemByStatus(ids, 1);
        return E3Result.ok();
    }

}
