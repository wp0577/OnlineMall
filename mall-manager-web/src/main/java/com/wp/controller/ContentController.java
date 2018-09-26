package com.wp.controller;

import com.wp.common.pojo.CatResult;
import com.wp.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<CatResult> getCatByPID(@RequestParam(defaultValue = "0", value = "id") long parentId) {
        List<CatResult> catByParentId = contentService.getCatByParentId(parentId);
        return catByParentId;
    }

}
