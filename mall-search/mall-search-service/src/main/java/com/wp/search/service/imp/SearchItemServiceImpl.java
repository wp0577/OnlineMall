package com.wp.search.service.imp;

import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.SearchItem;
import com.wp.search.service.SearchItemService;
import com.wp.search.service.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    //implemented the dao method defined within search-service module
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    /**
    * @Description: get all items and insert into solr server
    * @Param:
    * @return:
    * @Author: Pan wu
    * @Date: 9/30/18
    */
    @Override
    public E3Result getItem() {
        //查询商品列表
        try {
            List<SearchItem> itemList = itemMapper.getItemList();
            for (SearchItem searchItem:itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                //把文档对象写入索引库
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"Index process error");
        }
    }

    @Override
    public E3Result addDocument(long id) {
        try {
            SearchItem itemById = itemMapper.getItemById(id);
            SolrInputDocument document = new SolrInputDocument();
            //向文档中添加域
            document.addField("id", itemById.getId());
            document.addField("item_title", itemById.getTitle());
            document.addField("item_sell_point", itemById.getSell_point());
            document.addField("item_price", itemById.getPrice());
            document.addField("item_image", itemById.getImage());
            document.addField("item_category_name", itemById.getCategory_name());
            //把文档对象写入索引库
            solrServer.add(document);
            solrServer.commit();
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"Update document failed");
        }
    }
}
