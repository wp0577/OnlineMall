package com.wp.service.imp;

import com.wp.mapper.TbItemMapper;
import com.wp.pojo.TbItem;
import com.wp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
