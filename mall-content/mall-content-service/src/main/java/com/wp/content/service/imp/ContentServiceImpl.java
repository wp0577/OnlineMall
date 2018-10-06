package com.wp.content.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wp.common.pojo.CatResult;
import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.common.util.JsonUtils;
import com.wp.content.service.ContentService;
import com.wp.mapper.TbContentCategoryMapper;
import com.wp.mapper.TbContentMapper;
import com.wp.pojo.TbContent;
import com.wp.pojo.TbContentCategory;
import com.wp.pojo.TbContentCategoryExample;
import com.wp.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value(value = "${CONTENT_LIST}")
    private String CONTENT_LIST;

    /**
    * @Description: get webpage content category by parentId
    * @Param:  parentId
    * @return:  List
    * @Author: Pan wu
    * @Date: 9/26/18
    */
    @Override
    public List<CatResult> getCatByParentId(long parentId) {

        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categoryList = tbContentCategoryMapper.selectByExample(example);
        List<CatResult> catResults = new ArrayList<>();
        for (TbContentCategory category:categoryList) {
            CatResult catResult = new CatResult(category.getId(), category.getName(), category.getIsParent()?"closed":"open");
            catResults.add(catResult);
        }
        return catResults;
    }

    @Override
    public E3Result addCatByParentId(long parentId, String name) {
        //get original parent node to set isParent to 1 if not 1
        TbContentCategory categoryParent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!categoryParent.getIsParent()) {
            categoryParent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(categoryParent);
        }
        //add new node with parentId and name, status=1, sort_order=1, isParent=0
        TbContentCategory category = new TbContentCategory();
        category.setIsParent(false);
        category.setCreated(new Date());
        category.setName(name);
        category.setSortOrder(1);
        category.setUpdated(new Date());
        category.setParentId(parentId);
        category.setStatus(1);
        //we should return the id of new node to webpage, so we need to edit mapper.xml file
        tbContentCategoryMapper.insert(category);
        return E3Result.ok(category);
    }

    @Override
    public E3Result updateCatById(long id, String name) {
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
        category.setName(name);
        category.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKeySelective(category);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCatById(long id) {
        //需要检查如果有父节点需要需要检验父节点下是否还有其他节点若无则将父节点isParent=false。
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
        Long parentId = category.getParentId();
        //count # of parentNode to check whether this node isParent
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        int count = tbContentCategoryMapper.countByExample(example);
        if(count <= 1) {
            TbContentCategory category1 = tbContentCategoryMapper.selectByPrimaryKey(parentId);
            category1.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKeySelective(category1);
        }
        tbContentCategoryMapper.deleteByPrimaryKey(id);
        return E3Result.ok();
    }

    @Override
    public PageResult getContentPageByCatId(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
        PageResult pageResult = new PageResult();
        pageResult.setRows(tbContents);
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        pageResult.setTotal((int) pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public void addContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
        jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
    }

    @Override
    public void updateContent(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        tbContentMapper.updateByPrimaryKeySelective(tbContent);
        jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
    }

    @Override
    public void deleteContent(long[] ids) {
        for (long id: ids) {
            tbContentMapper.deleteByPrimaryKey(id);
        }
        // TODO: 9/30/18
        //delete redis by cid
    }

    @Override
    public List<TbContent> getContentByCatId(int i) {
        //first to check whether there is content data stored in redis
        //if it has, return list directly
        try {
            String content_list = jedisClient.hget(CONTENT_LIST, i + "");
            if(StringUtils.isNotBlank(content_list)) {
                return JsonUtils.jsonToList(content_list, TbContent.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //if not continue, and use try-catch to ensure the process will not be
        // interrupted by the error of redis server
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo((long)i);
        List<TbContent> tbContents = tbContentMapper.selectByExample(example);
        //if it don't have, store new data
        try {
            jedisClient.hset(CONTENT_LIST, i+"", JsonUtils.objectToJson(tbContents));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }
}
