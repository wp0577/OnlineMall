package com.wp.item.controller;

import com.wp.item.pojo.Item;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemDesc;
import com.wp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemDetailed(@PathVariable long itemId, Model model) {
        //get item and itemDesc by id
        TbItem itemById = itemService.getItemById(itemId);
        Item item = new Item(itemById);
        TbItemDesc itemDescById = itemService.getItemDescById(itemId);
        //because of src="${item.images[0] }, we should define new item object to implemented getImages method
        //return result
        model.addAttribute("item", item);
        model.addAttribute("itemDesc",itemDescById);
        return "item";
    }

}
