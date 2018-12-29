package com.muse.pay.service;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/8 19 10
 **/
public interface AsyncService {
    /**
     * 执行异步任务
     *
     * @param code
     */
    void executeAsync(String code);
}
