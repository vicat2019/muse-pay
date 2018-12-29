package com.nucc.service.alipay.impl;

import com.alibaba.fastjson.JSON;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.TradeInfoMapper;
import com.nucc.entity.NuccMessageInfo;
import com.nucc.entity.alipay.RefundInfo;
import com.nucc.entity.alipay.TradeInfo;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import com.nucc.entity.alipay.responseentity.*;
import com.nucc.service.activemq.ActiveMQService;
import com.nucc.service.alipay.NuccAlipayService;
import com.nucc.service.alipay.RefundInfoService;
import com.nucc.util.DateUtils;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;

/**
 * @program: muse-pay
 * @description: 网联阿里支付
 * @author: Vincent
 * @create: 2018-11-26 16:02
 **/
@Service("nuccAlipayService")
public class NuccAlipayServiceImpl extends BaseService<TradeInfoMapper, TradeInfo> implements NuccAlipayService {
    private Logger log = LoggerFactory.getLogger("NuccAlipayServiceImpl");

    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private ActiveMQService activeMQService;

    @Value("${seller_id}")
    private String sellerId;

    @Value("${seller_email}")
    private String sellerEmail;

    @Value("${qr_code_root_url}")
    private String qrCodeRootUrl;

    @Value("${delay_second}")
    private int delaySecond;

    /**
     * 刷卡支付
     *
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    @Override
    public BaseResponseEntity tradePay(TradeInfoVO tradePayParam) throws Exception {
        // 获取请求的信息
        TradeInfo tradeInfo = TradeInfo.getInstanceFrom(tradePayParam);
        if (!tradeInfo.legalParam().isOk()) {
            return tradeInfo.legalParam();
        }
        // 生成网联交易号
        tradeInfo.setTrade_no(TextUtils.makeTradeNo());

        // 是否已经提交过该订单
        TradeInfo exitTradeInfo = mapper.getTradeByOutTradeNo(tradeInfo.getOut_trade_no());
        if (exitTradeInfo != null) {
            log.error("订单已经提交过了[" + tradeInfo.getOut_trade_no() + "]");
            return BaseResponseEntity.getErrResult("订单号[" + tradeInfo.getOut_trade_no() + "]重复");
        }

        // 修改状态
        tradeInfo.setTrade_type("PAY");
        tradeInfo.setTrade_status("TRADE_SUCCESS");
        tradeInfo.setReceipt_amount(tradeInfo.getTotal_amount());
        tradeInfo.setOut_biz_no(TextUtils.getRandomNum(23));
        tradeInfo.setGmt_payment(new Date());
        tradeInfo.setGmt_close(new Date());
        // 卖家支付宝信息
        if (StringUtils.isEmpty(tradePayParam.getSeller_id())) {
            tradeInfo.setSeller_id(sellerId);
            tradeInfo.setSeller_id(sellerEmail);
        }

        // 保存
        mapper.insert(tradeInfo);

        // 发送回调通知消息
        sendNotifyMsg(tradeInfo.getTrade_no());

        // 返回内容
        TradePayResponse response = TradePayResponse.getInstanceFrom(tradeInfo);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity tradeQuery(TradeInfoVO tradePayParam) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(tradePayParam.getOut_trade_no()) || StringUtils.isEmpty(tradePayParam.getIdc_flag())) {
            return BaseResponseEntity.getErrResult("参数不能为空");
        }

        // 根据参数查询
        TradeInfo tradeInfo = mapper.getTradeByOutTradeNo(tradePayParam.getOut_trade_no());
        if (tradeInfo == null) {
            return BaseResponseEntity.getErrResult("查询的交易信息不存在");
        }

        // 生成返回内容
        TradePayResponse response = TradePayResponse.getInstanceFrom(tradeInfo);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity tradePrecreate(TradeInfoVO tradePayParam) throws Exception {
        TradeInfo tradeInfo = TradeInfo.getInstanceFrom(tradePayParam);
        if (!tradeInfo.legalParam().isOk()) {
            return tradeInfo.legalParam();
        }

        // 检查是否已经提交过
        TradeInfo exitTradeInfo = mapper.getTradeByOutTradeNo(tradeInfo.getOut_trade_no());
        if (exitTradeInfo != null) {
            return BaseResponseEntity.getErrResult("订单号[" + tradeInfo.getOut_trade_no() + "]不能重复");
        }

        // 设置交易信息值
        tradeInfo.setTrade_type("PRECREATE");
        tradeInfo.setTrade_status("WAIT_BUYER_PAY");
        tradeInfo.setTrade_no(TextUtils.makeTradeNo());
        tradeInfo.setReceipt_amount(tradeInfo.getTotal_amount());
        tradeInfo.setOut_biz_no(TextUtils.getRandomNum(23));
        // 生成二维码内容
        String qrCodeUrl = qrCodeRootUrl + "/alipay/qrcode/" + tradeInfo.getTrade_no();
        // 卖家支付宝信息
        if (StringUtils.isEmpty(tradePayParam.getSeller_id())) {
            tradeInfo.setSeller_id(sellerId);
            tradeInfo.setSeller_id(sellerEmail);
        }

        // 保存
        mapper.insert(tradeInfo);

        // 发送回调通知消息
        sendNotifyMsg(tradeInfo.getTrade_no());

        // 返回内容
        TradePrecreateResponse response = new TradePrecreateResponse(tradePayParam.getOut_trade_no(), qrCodeUrl);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity scanQrCode(String tradeNo) throws Exception {
        if (StringUtils.isEmpty(tradeNo)) {
            return BaseResponseEntity.getErrResult("交易编号不能为空");
        }

        // 检查交易是否存在
        TradeInfo tradeInfo = mapper.getTradeByTradeNo(tradeNo);
        if (tradeInfo == null) {
            return BaseResponseEntity.getErrResult("交易记录不存在");
        }

        // 修改支付状态为已经支付
        int count = mapper.updateTradeSucc(tradeInfo.getTrade_no());
        log.info("扫码支付，结果=" + count);
        if (count < 0) {
            return BaseResponseEntity.getErrResult("支付失败");
        } else {
            // 发送回调通知消息
            sendNotifyMsg(tradeInfo.getTrade_no());
            return BaseResponseEntity.getSuccessResult("支付成功");
        }
    }

    @Override
    public BaseResponseEntity tradeCreate(TradeInfoVO tradePayParam) throws Exception {
        TradeInfo tradeInfo = TradeInfo.getInstanceFrom(tradePayParam);
        if (!tradeInfo.legalParam().isOk()) {
            return tradeInfo.legalParam();
        }

        // 检查订单是否已经存在
        TradeInfo exitTradeInfo = mapper.getTradeByOutTradeNo(tradeInfo.getOut_trade_no());
        if (exitTradeInfo != null) {
            return BaseResponseEntity.getErrResult("订单号[" + tradeInfo.getOut_trade_no() + "]不能重复");
        }

        // 设置交易信息值
        tradeInfo.setTrade_type("CREATE");
        tradeInfo.setTrade_no(TextUtils.makeTradeNo());
        tradeInfo.setTrade_status("WAIT_BUYER_PAY");
        tradeInfo.setReceipt_amount(tradeInfo.getTotal_amount());
        tradeInfo.setOut_biz_no(TextUtils.getRandomNum(23));
        // 卖家支付宝信息
        if (StringUtils.isEmpty(tradePayParam.getSeller_id())) {
            tradeInfo.setSeller_id(sellerId);
            tradeInfo.setSeller_id(sellerEmail);
        }

        // 保存
        mapper.insert(tradeInfo);

        // 发送回调通知消息
        sendNotifyMsg(tradeInfo.getTrade_no());

        // 返回内容
        TradeCreatResponse response = new TradeCreatResponse();
        response.setOkValue();
        response.setOut_trade_no(tradeInfo.getOut_trade_no());
        response.setTrade_no(tradeInfo.getTrade_no());
        return response;
    }

    @Override
    public BaseResponseEntity tradeCancel(TradeInfoVO tradePayParam) throws Exception {
        return tradeClose(tradePayParam);
    }

    @Override
    public BaseResponseEntity tradeClose(TradeInfoVO tradePayParam) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(tradePayParam.getOut_trade_no())) {
            return BaseResponseEntity.getErrResult("参数不能为空");
        }

        // 查询交易信息
        TradeInfo tradeInfo = mapper.getTradeByOutTradeNo(tradePayParam.getOut_trade_no());
        if (tradeInfo == null) {
            return BaseResponseEntity.getErrResult("要关闭的交易信息不存在");
        }

        // 交易是否已经结束
        if ("TRADE_SUCCESS".equals(tradeInfo.getTrade_status())
                || "TRADE_FINISHED".equals(tradeInfo.getTrade_status())) {
            return BaseResponseEntity.getErrResult("该交易已经结束");
        }

        // 关闭订单
        mapper.closeTrade(tradePayParam.getOut_trade_no());

        // 发送回调通知消息
        sendNotifyMsg(tradeInfo.getTrade_no());

        // 返回内容
        TradeCancelResponse response = TradeCancelResponse.getDefaultBy(tradeInfo);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity tradeRefund(TradeInfoVO tradePayParam) throws Exception {
        // 获取要保存的退款信息
        RefundInfo refundInfo = RefundInfo.getByParams(tradePayParam);

        // 查询交易记录
        TradeInfo tradeInfo = mapper.getTradeByOutTradeNo(tradePayParam.getOut_trade_no());
        if (tradeInfo == null) {
            log.error("申请退款，交易记录为空[" + tradePayParam.getOut_trade_no() + "]");
            return BaseResponseEntity.getErrResult("要退款的交易信息为空");
        }
        refundInfo.setTrade_no(tradeInfo.getTrade_no());
        refundInfo.setTotal_amount(tradeInfo.getTotal_amount());

        // 保存退款记录
        ResultData result = refundInfoService.add(refundInfo);
        if (!result.isOk()) {
            log.error("保存退款记录异常=" + result.getMessage());
            return BaseResponseEntity.getErrResult(result.getMessage());
        }

        // 返回结果
        TradeRefundResponse response = TradeRefundResponse.getInstanceFrom(refundInfo, tradeInfo);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity tradeFastpayRefundQuery(TradeInfoVO tradePayParam) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(tradePayParam.getOut_trade_no()) || StringUtils.isEmpty(tradePayParam.getOut_request_no())) {
            return BaseResponseEntity.getErrResult("参数为空，out_trade_no=" + tradePayParam.getOut_trade_no()
                    + ", out_request_no=" + tradePayParam.getOut_request_no());
        }

        // 根据OUT_TRADE_NO查询退款记录
        ResultData result = refundInfoService.queryRefundByKey(tradePayParam.getOut_trade_no(),
                tradePayParam.getOut_request_no());
        if (!result.isOk()) {
            return BaseResponseEntity.getErrResult(result.getMessage());
        }
        if (result.resultIsEmpty()) {
            return BaseResponseEntity.getErrResult("没有退款记录[" + tradePayParam.getOut_request_no() + "]");
        }

        // 根据退款记录生成返回对象
        RefundInfo refundInfo = (RefundInfo) result.getData();
        return TradeRefundResponse.getQueryResponse(refundInfo);
    }

    /**
     * 发送MQ消息
     *
     * @param tradeNo
     * @return
     */
    public BaseResponseEntity sendNotifyMsg(String tradeNo) {
        // 检查参数
        if (StringUtils.isEmpty(tradeNo)) {
            return BaseResponseEntity.getErrResult("参数不能为空");
        }
        // 查询订单是否存在
        TradeInfo tradeInfo = mapper.getTradeByTradeNo(tradeNo);
        if (tradeInfo == null) {
            return BaseResponseEntity.getErrResult("该订单[" + tradeNo + "]不存在");
        }

        // 生成通知记录
        NuccMessageInfo messageInfo = new NuccMessageInfo();
        String msgNo = DateUtils.getCurrent("yyyyMMddHHmmss") + TextUtils.getRandomNum(14);
        messageInfo.setMsgNo(msgNo);
        messageInfo.setMsgType(NuccMessageInfo.MSG_TYPE_ALI_PAY);
        messageInfo.setTimestamp(new Date());
        messageInfo.setDelayTime(delaySecond);
        messageInfo.setMsgContent(JSON.toJSONString(tradeInfo));
        log.info("sendNotifyMsg() 生成消息=" + messageInfo);

        try {
            activeMQService.sendMessage(messageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送消息到MQ，异常=" + e.getMessage());
        }

        return BaseResponseEntity.getSuccessResult();
    }

}
