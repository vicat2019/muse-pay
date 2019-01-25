package com.nucc.service.alipay;

import com.muse.common.entity.ResultData;
import com.nucc.entity.alipay.RefundInfo;

public interface RefundInfoService {

    ResultData add(RefundInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(RefundInfo obj) throws Exception;

    ResultData queryRefundByKey(String outTradeNo, String outRequestNo) throws Exception;

}
