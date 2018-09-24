package com.wp.controller;

import com.wp.common.pojo.CatResult;
import com.wp.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: WpMall
 * @description: manager item category
 * @author: Pan wu
 * @create: 2018-09-24 11:14
 **/
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    //default value of id should be 0, because the ajax request from web is to get parent root
    public List<CatResult> getCatByParentId(@RequestParam(defaultValue = "0") int id) {
        List<CatResult> list = itemCatService.getCatByParentId(id);
        return list;
    }

}
