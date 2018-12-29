package com.nucc.service.alipay;

import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;

public interface NuccAlipayService {

    /**
     * 统一收单交易-支付（刷卡）
     *
     * @param tradePayParam
     * @return TradePayResponse
     * @throws Exception
     */
    BaseResponseEntity tradePay(TradeInfoVO tradePayParam) throws Exception;

    /**
     * 统一收单线下交易-查询
     *
     * @param tradePayParam
     * @return TradePayResponse
     * @throws Exception
     */
    BaseResponseEntity tradeQuery(TradeInfoVO tradePayParam) throws Exception;


    /**
     * 统一收单线下交易-预创建（扫码）
     *
     * @param tradePayParam
     * @return TradePrecreateResponse
     * @throws Exception
     */
    BaseResponseEntity tradePrecreate(TradeInfoVO tradePayParam) throws Exception;


    /**
     * 统一收单线下交易-创建
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    BaseResponseEntity tradeCreate(TradeInfoVO tradePayParam) throws Exception;

    /**
     * 统一收单交易-撤销
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    BaseResponseEntity tradeCancel(TradeInfoVO tradePayParam) throws Exception;

    /**
     * 统一收单交易-退款
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    BaseResponseEntity tradeRefund(TradeInfoVO tradePayParam) throws Exception;


    /**
     * 统一收单交易-退款查询
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    BaseResponseEntity tradeFastpayRefundQuery(TradeInfoVO tradePayParam) throws Exception;

    /**
     * 统一收单交易关闭
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    BaseResponseEntity tradeClose(TradeInfoVO tradePayParam) throws Exception;

    /**
     * 扫描二维码支付
     * @param tradeNo
     * @return
     * @throws Exception
     */
    BaseResponseEntity scanQrCode(String tradeNo) throws Exception;


}
