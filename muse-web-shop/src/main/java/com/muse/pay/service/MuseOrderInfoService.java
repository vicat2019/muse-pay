package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.MuseOrderInfo;


/**
 * Created by code generator  on 2018/08/08.
 */
public interface MuseOrderInfoService {


    ResultData add(MuseOrderInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(MuseOrderInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData<MuseOrderInfo> getByOrderNo(String orderNo) throws Exception;

    boolean isExists(String merchantNo, String orderNo) throws Exception;

    ResultData<MuseOrderInfo> getByMerchantAndOrderCode(String merchantNo, String orderNo) throws Exception;
}
