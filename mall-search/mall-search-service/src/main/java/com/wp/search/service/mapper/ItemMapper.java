package com.wp.search.service.mapper;

import com.wp.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
