package com.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    /**
    * @Description: return index.jsp when enter uri
    * @Param:  null
    * @return:  jsp/index.jsp
    * @Author: Pan wu
    * @Date: 9/24/18
    */
    @RequestMapping("/")
    public String showIndex() {
        return "index";
    }

    @RequestMapping("/{page}")
    public String getPageByUrl(@PathVariable String page) {
        return page;
    }

    @RequestMapping("/rest/page/item-edit")
    public String getItemEditPage() {return "item-edit";}

}
