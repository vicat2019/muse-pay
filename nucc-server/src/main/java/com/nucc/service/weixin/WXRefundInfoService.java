package com.nucc.service.weixin;


import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.requestentity.WXRefundInfoVO;

/**
 * Created by code generator  on 2018/12/02.
 */
public interface WXRefundInfoService {

    WXBaseResponse add(WXRefundInfoVO obj) throws Exception;

    WXBaseResponse del(Integer id) throws Exception;

    WXBaseResponse querySingleRefund(WXRefundInfoVO obj) throws Exception;

    WXBaseResponse queryRefund(WXRefundInfoVO obj) throws Exception;

    WXBaseResponse get(Integer id) throws Exception;
}
