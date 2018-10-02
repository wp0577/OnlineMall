package com.wp.search.controller;

import com.wp.common.pojo.SolrSearchResult;
import com.wp.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: WpMall
 * @description: query for index
 * @author: Pan wu
 * @create: 2018-10-01 17:29
 **/

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer search_result_rows;

    @RequestMapping("/search")
    public String SearchForSolr(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        //because of get method, the keyword can't not recognize chinese
        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        SolrSearchResult searchResult = searchService.search(keyword, page, search_result_rows);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("recourdCount", searchResult.getRecourdCount());
        model.addAttribute("itemList", searchResult.getItemList());
        //Global exception test
        //int a = 1/0;
        return "search";
    }

}
