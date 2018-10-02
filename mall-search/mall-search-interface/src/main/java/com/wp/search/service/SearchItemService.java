package com.wp.search.service;

import com.wp.common.pojo.E3Result;

public interface SearchItemService {
    E3Result getItem();
    E3Result addDocument(long id);
}
