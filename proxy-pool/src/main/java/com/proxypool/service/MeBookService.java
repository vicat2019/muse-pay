package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.kindlebook.MeBookInfo;

public interface MeBookService {

    ResultData insert(MeBookInfo bookInfo) throws Exception;

    ResultData update(MeBookInfo bookInfo) throws Exception;
}
