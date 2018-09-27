package com.wp.portal.controller;

import com.wp.content.service.ContentService;
import com.wp.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index.html")
    public String getIndexPage(Model model) {
        //return index with ad1List
        List<TbContent> tbContentList = contentService.getContentByCatId(89);
        model.addAttribute("ad1List", tbContentList);
        return "index";
    }

}
