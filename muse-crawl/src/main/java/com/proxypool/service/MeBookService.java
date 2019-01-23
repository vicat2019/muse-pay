package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.kindlebook.MeBookInfo;

import java.util.List;

public interface MeBookService {

    ResultData insert(MeBookInfo bookInfo) throws Exception;

    ResultData update(MeBookInfo bookInfo) throws Exception;

    ResultData handleMeBook() throws Exception;

    List<Integer> getAllCode() throws Exception;

    int getCountByCode(int code) throws Exception;
}
