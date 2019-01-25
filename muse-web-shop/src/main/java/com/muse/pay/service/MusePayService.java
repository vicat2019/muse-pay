package com.muse.pay.service;


import com.muse.common.entity.ResultData;
import com.muse.pay.entity.common.UnifiedOrderResultData;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/13 14 45
 **/
public interface MusePayService {

    /**
     * 统一下单
     *
     * @param paramContent 参数内容
     * @return ResultData
     * @throws Exception 异常
     */
    UnifiedOrderResultData unifiedOrder(String paramContent) throws Exception;


    /**
     * 支付接口
     *
     * @param orderNo 订单编号
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData<String> pay(String orderNo) throws Exception;

    /**
     * 查询订单
     * @param content 字符串参数
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData queryOrder(String content) throws Exception;


}
