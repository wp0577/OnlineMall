package com.wp.content.service.imp;

import com.wp.common.pojo.CatResult;
import com.wp.content.service.ContentService;
import com.wp.mapper.TbContentCategoryMapper;
import com.wp.pojo.TbContentCategory;
import com.wp.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

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
}
