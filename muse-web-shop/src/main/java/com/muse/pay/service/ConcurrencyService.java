package com.muse.pay.service;

import com.muse.common.entity.ResultData;

/**
 * 并发测试服务接口
 */
public interface ConcurrencyService {

    /**
     * 并发测试
     *
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData concurrency(String userId, String bookId) throws Exception;
}
