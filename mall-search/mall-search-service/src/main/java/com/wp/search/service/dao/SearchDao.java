package com.wp.search.service.dao;

import com.wp.common.pojo.SearchItem;
import com.wp.common.pojo.SolrSearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @program: WpMall
 * @description: search api for solr
 * @author: Pan wu
 * @create: 2018-10-01 17:49
 **/
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SolrSearchResult search(SolrQuery query) throws SolrServerException {
        //pass query to dao by service
        //get result by query
        QueryResponse queryResponse = solrServer.query(query);
        //get total number of result
        SolrDocumentList results = queryResponse.getResults();
        long numFound = results.getNumFound();
        //create SolrSearchResult object
        SolrSearchResult searchResult = new SolrSearchResult();
        searchResult.setRecourdCount((int) numFound);
        //create item list
        List<SearchItem> items = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : results) {
            //取商品信息
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            //get highlight content
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(itemTitle);
            items.add(searchItem);
        }
        searchResult.setItemList(items);
        return searchResult;
    }

}
