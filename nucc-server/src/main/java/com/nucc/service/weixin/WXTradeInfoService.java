package com.nucc.service.weixin;

import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXTradeInfo;


/**
 * Created by code generator  on 2018/12/02.
 */
public interface WXTradeInfoService {

    WXBaseResponse micropay(WXTradeInfo obj) throws Exception;

    WXBaseResponse cancelOrder(WXTradeInfo obj) throws Exception;

    WXBaseResponse closeOrder(WXTradeInfo obj) throws Exception;

    WXBaseResponse update(WXTradeInfo obj) throws Exception;

    WXBaseResponse queryOrder(WXTradeInfo obj) throws Exception;

    WXBaseResponse unifiedOrder(WXTradeInfo obj) throws Exception;

    WXTradeInfo getTradeByOutTradeNo(String outTradeNo) throws Exception;

    WXBaseResponse authCodeToOpenId(WXTradeInfo obj) throws Exception;

    WXBaseResponse sendNotifyMsg(String subMchId, String outTradeNo);

    WXTradeInfo getTradeByTransactionId(String transactionId) throws Exception;

    WXBaseResponse scanQrcode(String transactionId) throws Exception;

}
