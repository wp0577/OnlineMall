package com.wp.controller;

import com.wp.common.pojo.CatResult;
import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.content.service.ContentService;
import com.wp.pojo.TbContent;
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

    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result saveCatByPID(long parentId, String name) {
        E3Result e3Result = contentService.addCatByParentId(parentId, name);
        return e3Result;
    }

    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updateCatByID(long id, String name) {
        E3Result e3Result = contentService.updateCatById(id, name);
        return e3Result;
    }

    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public E3Result deletCatByID(long id) {
        E3Result e3Result = contentService.deleteCatById(id);
        return e3Result;
    }

    /**
    * @Description: get page of content of each category
    * @Param:  categoryId, page, rows
    * @return:  PageResult
    * @Author: Pan wu
    * @Date: 9/26/18
    */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public PageResult getContentPage(long categoryId, int page, int rows) {
        PageResult pageResult = contentService.getContentPageByCatId(categoryId, page, rows);
        return pageResult;
    }

    /*categoryId: 90 title: sadas subTitle: asdas titleDesc: asd url: asd  pic:  pic2: content: sad*/

    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result addContent(TbContent tbContent) {
        contentService.addContent(tbContent);
        return E3Result.ok();
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public E3Result updateContent(TbContent tbContent) {
        contentService.updateContent(tbContent);
        return E3Result.ok();
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public E3Result deleteContent(long[] ids) {
        contentService.deleteContent(ids);
        return E3Result.ok();
    }



}
