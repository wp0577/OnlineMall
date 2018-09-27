package com.wp.content.service;

import com.wp.common.pojo.CatResult;
import com.wp.common.pojo.E3Result;
import com.wp.common.pojo.PageResult;
import com.wp.pojo.TbContent;

import java.util.List;

public interface ContentService {
    List<CatResult> getCatByParentId(long parentId);
    E3Result addCatByParentId(long parentId, String name);
    E3Result updateCatById(long id, String name);

    E3Result deleteCatById(long id);

    PageResult getContentPageByCatId(long categoryId, int page, int rows);

    void addContent(TbContent tbContent);

    void updateContent(TbContent tbContent);

    void deleteContent(long[] ids);

    List<TbContent> getContentByCatId(int i);
}
