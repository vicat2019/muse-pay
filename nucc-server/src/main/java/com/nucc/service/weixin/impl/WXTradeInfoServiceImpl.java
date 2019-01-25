package com.nucc.service.weixin.impl;

import com.alibaba.fastjson.JSON;
import com.nucc.dao.weixin.WXTradeInfoMapper;
import com.nucc.entity.NuccMessageInfo;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXTradeInfo;
import com.nucc.entity.weixin.responseentity.WXTradeResponse;
import com.nucc.service.activemq.ActiveMQServiceImpl;
import com.nucc.service.alipay.BaseService;
import com.nucc.service.weixin.WXTradeInfoService;
import com.nucc.util.DateUtils;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by code generator  on 2018/12/02.
 */
@Service("wxTradeInfoService")
@Transactional
public class WXTradeInfoServiceImpl extends BaseService<WXTradeInfoMapper, WXTradeInfo> implements WXTradeInfoService {
    private Logger log = LoggerFactory.getLogger("WXTradeInfoServiceImpl");

    @Autowired
    private ActiveMQServiceImpl activeMQService;

    @Value("${qr_code_root_url}")
    private String qrCodeRootUrl;

    @Value("${delay_second}")
    private int delaySecond;

    @Override
    public WXBaseResponse micropay(WXTradeInfo tradeInfo) throws Exception {
        // 检查参数
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }
        // 检查该订单是否已经存在
        WXTradeInfo exitTrade = mapper.getWXTradeByOutTradeNo(tradeInfo.getOut_trade_no());
        if (exitTrade != null) {
            return WXBaseResponse.getErrResult("商户订单号[" + tradeInfo.getOut_trade_no() + "]已存在");
        }

        // 将状态设置成支付成功状态
        tradeInfo.setTrade_status("SUCCESS");
        tradeInfo.setTrade_state_desc("支付成功");
        // 支付完成时间
        tradeInfo.setTime_end(DateUtils.getCurrent("yyyy-MM-dd HH:mm:ss"));
        // 生成网联订单号
        tradeInfo.setTransaction_id(DateUtils.getCurrent("yyyyMMddHHmmssSSS") + TextUtils.getRandomNum(11));
        // 现金支付金额
        tradeInfo.setCash_fee(tradeInfo.getTotal_fee());
        tradeInfo.setCash_fee_type("CNY");
        tradeInfo.setFee_type("CNY");
        // 银行类型
        tradeInfo.setBank_type("CITIC_DEBIT");
        tradeInfo.setTrade_status("SUCCESS");
        tradeInfo.setSettlement_total_fee(tradeInfo.getTotal_fee());

        // 保存记录
        int result = mapper.insert(tradeInfo);
        log.info("刷卡支付，添加订单=" + result);

        // 发送回调通知
        sendNotifyMsg(tradeInfo.getSub_mch_id(), tradeInfo.getOut_trade_no());

        // 返回结果
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setMicroPayResponse(tradeInfo);
        return response;
    }

    @Override
    public WXBaseResponse queryOrder(WXTradeInfo obj) throws Exception {
        // 检查参数
        if (obj == null || StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("查询参数不能为空");
        }

        // 查询记录
        WXTradeInfo tradeInfo = mapper.getWXTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("交易记录[" + obj.getOut_trade_no() + "]不存在");
        }

        // 返回结果
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setTradeQueryResposnse(tradeInfo);
        return response;
    }

    @Override
    public WXBaseResponse unifiedOrder(WXTradeInfo obj) throws Exception {
        // 检查参数
        if (obj == null || StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("查询参数不能为空");
        }

        // 检查是否已经存在
        Integer count = mapper.getCountByOutTradeNo(obj.getOut_trade_no());
        if (count != null && count > 0) {
            return WXBaseResponse.getErrResult("商户订单号[" + obj.getOut_trade_no() + "]已经存在");
        }

        // 生成网联订单号
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        obj.setTransaction_id(sdf2.format(new Date()) + TextUtils.getRandomNum(11));
        // 设置二维码
        String qrCode = qrCodeRootUrl + "/weixin/pay/qrcode/" + obj.getTransaction_id();
        // 生成PERPAY_ID
        String perpayId = "wx" + sdf2.format(new Date()) + TextUtils.getRandomNum(17);
        obj.setPerpay_id(perpayId);

        // 保存
        int result = mapper.insert(obj);
        log.info("统一下单，结果=" + result);

        // 返回内容
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setUnifiedResponse(obj, qrCode);
        return response;
    }

    @Override
    public WXTradeInfo getTradeByOutTradeNo(String outTradeNo) throws Exception {
        if (StringUtils.isEmpty(outTradeNo)) {
            return null;
        }

        return mapper.getWXTradeByOutTradeNo(outTradeNo);
    }

    @Override
    public WXBaseResponse scanQrcode(String transactionId) throws Exception {
        if (StringUtils.isEmpty(transactionId)) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }
        // 查询订单是否存在
        WXTradeInfo tradeInfo = getTradeByTransactionId(transactionId);
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("该订单[" + transactionId + "]不存在");
        }

        // 更新支付状态
        tradeInfo.setTrade_status("SUCCESS");
        tradeInfo.setTrade_state_desc("支付成功");
        // 支付完成时间
        tradeInfo.setTime_end(DateUtils.getCurrent("yyyy-MM-dd HH:mm:ss"));
        // 现金支付金额
        tradeInfo.setCash_fee(tradeInfo.getTotal_fee());
        tradeInfo.setCash_fee_type("CNY");
        tradeInfo.setFee_type("CNY");
        // 银行类型
        tradeInfo.setBank_type("CITIC_DEBIT");
        tradeInfo.setTrade_status("SUCCESS");
        tradeInfo.setSettlement_total_fee(tradeInfo.getTotal_fee());

        // 更新信息
        int updateResult = mapper.updateByTransactionId(tradeInfo);
        log.info("预下单支付后更新订单状态，结果=" + updateResult);

        // 发送回调消息
        sendNotifyMsg(tradeInfo.getSub_mch_id(), tradeInfo.getOut_trade_no());

        // 返回结果
        return WXBaseResponse.getSuccessResult("支付成功");
    }

    @Override
    public WXTradeInfo getTradeByTransactionId(String transactionId) throws Exception {
        if (StringUtils.isEmpty(transactionId)) {
            return null;
        }

        return mapper.getTradeByTransactionId(transactionId);
    }

    @Override
    public WXBaseResponse cancelOrder(WXTradeInfo obj) throws Exception {
        if (obj == null || StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("撤销参数不能为空");
        }

        // 检查订单是否存在
        WXTradeInfo tradeInfo = mapper.getWXTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("该交易[" + obj.getOut_trade_no() + "]不存在");
        }

        // 撤销订单
        int result = mapper.setTradePayStatus(obj.getOut_trade_no(), "REVOKED");
        log.info("撤销订单[" + obj.getOut_trade_no() + "]，结果=" + result);

        // 返回内容
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setCancelResponse(tradeInfo, "N");
        return response;
    }

    @Override
    public WXBaseResponse closeOrder(WXTradeInfo obj) throws Exception {
        if (obj == null || StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("关闭订单参数不能为空");
        }

        // 检查订单是否存在
        WXTradeInfo tradeInfo = mapper.getWXTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("该交易[" + obj.getOut_trade_no() + "]不存在");
        }

        // 撤销订单
        int result = mapper.setTradePayStatus(obj.getOut_trade_no(), "CLOSED");
        log.info("关闭订单[" + obj.getOut_trade_no() + "]，结果=" + result);

        // 返回内容
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setCancelResponse(tradeInfo, "");
        return response;
    }

    @Override
    public WXBaseResponse update(WXTradeInfo obj) throws Exception {
        if (obj == null) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return WXBaseResponse.getErrResult("ID不能为空");
        }

        int result = mapper.updateByTransactionId(obj);
        return WXBaseResponse.getSuccessResult();
    }


    @Override
    public WXBaseResponse authCodeToOpenId(WXTradeInfo obj) throws Exception {
        if (obj == null || StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("关闭订单参数不能为空");
        }

        // 返回内容
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setAuthCodeToOpenIdResponse(obj);
        return response;
    }


    @Override
    public WXBaseResponse sendNotifyMsg(String subMchId, String outTradeNo) {
        // 检查参数
        if (StringUtils.isEmpty(subMchId) || StringUtils.isEmpty(outTradeNo)) {
            log.error("发起回调异常，参数不能为空，subMchId=" + subMchId + ", outTradeNo" + outTradeNo);
            return WXBaseResponse.getErrResult("参数不能为空");
        }
        // 查询支付记录
        WXTradeInfo tradeInfo = mapper.getWXTradeByOutTradeNo(outTradeNo);
        if (tradeInfo == null) {
            log.error("发起回调异常，订单[" + outTradeNo + "]不存在");
            return WXBaseResponse.getErrResult("订单[" + outTradeNo + "]不存在");
        }
        // 是否有回调地址
        if (StringUtils.isEmpty(tradeInfo.getNotify_url())) {
            log.error("发起回调异常，回调地址为空");
            return WXBaseResponse.getErrResult("订单[" + outTradeNo + "]的回调地址为空");
        }

        // 生成通知记录
        NuccMessageInfo messageInfo = new NuccMessageInfo();
        String msgNo = DateUtils.getCurrent("yyyyMMddHHmmss") + TextUtils.getRandomNum(14);
        messageInfo.setMsgNo(msgNo);
        messageInfo.setMsgType(NuccMessageInfo.MSG_TYPE_WX_PAY);
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

        return WXBaseResponse.getSuccessResult();
    }


}
