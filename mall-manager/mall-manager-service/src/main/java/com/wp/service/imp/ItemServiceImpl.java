package com.wp.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.common.util.IDUtils;
import com.wp.mapper.TbItemDescMapper;
import com.wp.mapper.TbItemMapper;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemDesc;
import com.wp.pojo.TbItemExample;
import com.wp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

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

    @Override
    public E3Result saveItem(TbItem tbItem, String desc) {
        //we use IdUtil to set id automatically because id do not autoincrement in database
        tbItem.setId(IDUtils.genItemId());
        //Item status，1-normal，2-no available，3-delete
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItemMapper.insert(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.insert(tbItemDesc);
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long id) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        return tbItemDesc;
    }

    @Override
    public void updateItem(TbItem tbItem, String desc) {
        //get created time first
        Date created1 = tbItemMapper.selectByPrimaryKey(tbItem.getId()).getCreated();
        Date created2 = tbItemDescMapper.selectByPrimaryKey(tbItem.getId()).getCreated();
        //update item
        tbItem.setCreated(created1);
        tbItem.setUpdated(new Date());
        tbItem.setStatus((byte) 1);
        tbItemMapper.updateByPrimaryKey(tbItem);
        //update itemdesc
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(created2);
        tbItemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);
    }

    @Override
    public void deleteItemById(long[] ids) {
        for(long id : ids) {
            tbItemMapper.deleteByPrimaryKey(id);
            tbItemDescMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void updateItemByStatus(long[] ids, int i) {
        for(long id : ids) {
            //get original item
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
            tbItem.setStatus((byte) i);
            tbItemMapper.updateByPrimaryKey(tbItem);
        }
    }
}
