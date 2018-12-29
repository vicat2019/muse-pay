package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.MuseMerchantInfo;


/**
 * Created by code generator  on 2018/08/08.
 */
public interface MuseMerchantInfoService {

    ResultData add(MuseMerchantInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(MuseMerchantInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData getByUserNo(String userNo) throws Exception;

    ResultData getSecretByUserNo(String userNo) throws Exception;

    ResultData list(MuseMerchantInfo merchantInfo, int pageNum) throws Exception;
}
