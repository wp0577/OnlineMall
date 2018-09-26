package com.wp.content.service;

import com.wp.common.pojo.CatResult;

import java.util.List;

public interface ContentService {
    List<CatResult> getCatByParentId(long parentId);
}
