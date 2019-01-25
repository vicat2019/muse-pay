package com.muse.pay.service;

import com.muse.common.entity.ResultData;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/13 18 09
 **/
public interface OrderPayService {


    /**
     * 支付
     *
     * @param userId    用户ID
     * @param orderNo 订单编号
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData<String> doPay(int userId, String orderNo) throws Exception;

    /**
     * 支付完成的回调通知
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData<Void> payNotify(Map<String, String> notifyParamMap, HttpSession session) throws Exception;


    ResultData doRechargePay(int userId, String orderNo) throws Exception;


}
