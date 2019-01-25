package com.nucc.service.weixin;

import com.muse.common.entity.ResultData;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.requestentity.WXMerchantInfoVO;


/**
 * Created by code generator  on 2018/11/29.
 */
public interface WXMerchantInfoService {

    WXBaseResponse add(WXMerchantInfoVO obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    WXBaseResponse update(WXMerchantInfoVO obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    WXBaseResponse query(WXMerchantInfoVO obj) throws Exception;
}
