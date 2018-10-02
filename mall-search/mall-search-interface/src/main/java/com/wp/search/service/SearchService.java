package com.wp.search.service;

import com.wp.common.pojo.SolrSearchResult;

/**
 * @program: WpMall
 * @description: the service related to get search result in webpage
 * @author: Pan wu
 * @create: 2018-10-01 19:22
 **/
public interface SearchService {
    SolrSearchResult search(String keyword, Integer page, Integer rows) throws Exception;
}
