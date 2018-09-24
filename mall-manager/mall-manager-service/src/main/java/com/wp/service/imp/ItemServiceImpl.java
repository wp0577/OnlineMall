package com.wp.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wp.common.pojo.PageResult;
import com.wp.mapper.TbItemMapper;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemExample;
import com.wp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-09-22 11:13
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public TbItem getItemById(Long id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult getItemPage(int page, int rows) {
        //只要调用了startPage方法，接下来通过select得到的list就会遵循startPage中的属性
        PageHelper.startPage(page, rows);
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        PageResult pageResult = new PageResult();
        pageResult.setRows(tbItems);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        pageResult.setTotal((int)pageInfo.getTotal());
        return pageResult;
    }
}
