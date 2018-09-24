package com.wp.service.imp;

import com.wp.common.pojo.CatResult;
import com.wp.mapper.TbItemCatMapper;
import com.wp.pojo.TbItemCat;
import com.wp.pojo.TbItemCatExample;
import com.wp.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-09-24 12:13
 **/
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<CatResult> getCatByParentId(long parentId) {
        //get cat of item by parentId;
        TbItemCatExample catExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = catExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(catExample);
        //create list of CatResult
        List<CatResult> list = new ArrayList<>();
        for (TbItemCat tbItemCat:tbItemCats) {
            CatResult catResult = new CatResult(tbItemCat.getId(), tbItemCat.getName(),
                    tbItemCat.getIsParent()?"closed":"open");
            list.add(catResult);
        }
        return list;
    }
}
