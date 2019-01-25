package com.nucc.service.weixin;

import com.muse.common.entity.ResultData;
import com.nucc.entity.weixin.WXSuperMerchantInfo;


/**
 * Created by code generator  on 2018/12/04.
 */
public interface WXSuperMerchantInfoService {

    ResultData add(WXSuperMerchantInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(WXSuperMerchantInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData getMerchantByAppId(String appId) throws Exception;
}
