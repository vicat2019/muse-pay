package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.MerchantInfo;


/**
 * Created by code generator  on 2018/09/26.
 */
public interface MerchantInfoService {

    ResultData add(MerchantInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(MerchantInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData setStatus(Integer id, String status) throws Exception;

    ResultData getRandomActiveMerchant() throws Exception;

    ResultData queryMerchant(String name, String status, String payKey, String paySecret, int pageNum, int pageSize) throws Exception;
}
