package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.BookInfo;

import java.util.List;


/**
 * Created by code generator  on 2018/07/22.
 */
public interface BookInfoService {

    ResultData add(BookInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(BookInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData list(BookInfo bookInfo, int pageNum, int pageSize) throws Exception;

    ResultData queryBookIn(List<Integer> ids) throws Exception;

    ResultData getRecommend(int size) throws Exception;

    ResultData genRandomBookFile(int count, String path) throws Exception;
}
