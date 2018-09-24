package com.wp.service;

import com.wp.common.pojo.CatResult;

import java.util.List;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-09-24 12:12
 **/
public interface ItemCatService {

    List<CatResult> getCatByParentId(long id);
}
