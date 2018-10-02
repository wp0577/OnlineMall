package com.wp.search.service.imp;

import com.wp.common.pojo.SolrSearchResult;
import com.wp.search.service.SearchService;
import com.wp.search.service.dao.SearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-01 19:24
 **/
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SolrSearchResult search(String keyword, Integer page, Integer rows) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //set query condition
        solrQuery.setQuery(keyword);
        //set page condition
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        //set default query field
        solrQuery.set("df", "item_title");
        //set highlight filed
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        SolrSearchResult search = searchDao.search(solrQuery);
        //count total pages
        int recourdCount = search.getRecourdCount();
        int pages = recourdCount / rows;
        if(recourdCount % rows > 0) pages++;
        search.setTotalPages(pages);
        return search;
    }
}
