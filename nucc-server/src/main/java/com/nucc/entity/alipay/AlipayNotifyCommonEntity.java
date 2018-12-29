package com.nucc.entity.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 支付宝回调内容
 * @author: Vincent
 * @create: 2018-12-08 15:08
 **/
public class AlipayNotifyCommonEntity {


    public static Map<String, Object> getAlipayPayNotify(TradeInfo tradeInfo) {
        Map<String, Object> response = new HashMap<>();

        // 网联分配给收单机构的pid
        response.put("pid", getValue(tradeInfo.getPid()));
        response.put("trade_no", getValue(tradeInfo.getTrade_no()));
        response.put("out_trade_no", getValue(tradeInfo.getOut_trade_no()));

        // 商户业务号
        response.put("out_biz_no", getValue(tradeInfo.getOut_biz_no()));
        response.put("buyer_logon_id", getValue(tradeInfo.getBuyer_logon_id()));

        // 买家支付宝用户号
        response.put("seller_id", getValue(tradeInfo.getSeller_id()));
        // 买家支付宝账号
        response.put("seller_email", getValue(tradeInfo.getSeller_email()));
        response.put("total_amount", getValue(tradeInfo.getTotal_amount()));
        response.put("buyer_id", getValue(tradeInfo.getBuyer_user_id()));
        response.put("trade_status", getValue(tradeInfo.getTrade_status()));
        response.put("invoice_amount", getValue(tradeInfo.getInvoice_amount()));
        response.put("receipt_amount", getValue(tradeInfo.getReceipt_amount()));
        response.put("point_amount", getValue(tradeInfo.getPoint_amount()));

        // 退款金额
        response.put("refund_fee", "");
        response.put("buyer_pay_amount", getValue(tradeInfo.getBuyer_pay_amount()));
        // 订单标题
        response.put("subject", getValue(tradeInfo.getSubject()));
        // 商品描述
        response.put("body", getValue(tradeInfo.getBody()));

        // 交易创建时间
        response.put("gmt_create", getValue(tradeInfo.getCreateTime()));
        // 交易支付时间
        response.put("gmt_payment", getValue(tradeInfo.getGmt_payment()));
        // 交易退款时间
        response.put("gmt_refund", "");
        // 交易结束时间
        response.put("gmt_close", getValue(tradeInfo.getGmt_close()));

        // 支付金额信息"[{\"amount\":\"10000.00\",\"fundChannel\":\"ALIPAYACCOUNT\"}]"
        response.put("fund_bill_list", "[{\"amount\":\"10000.00\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
        // 本交易支付时使用的所有优惠券信息"[{\"id\":\"20170307000730026487005X1M6V\",\"name\":\" 全 仓 5折优惠券\"}]"
        response.put("voucher_detail_list", "");
        // 本次交易支付所使用的单品优惠券的商品优惠信息
        // [{"goods_id":"STANDARD1026181538","goods_name":" 雪 碧","discount_amount":"100.00","voucher_id":"2015102600073002039000002D5O"}]
        response.put("discount_goods_detail", "");
        // 预授权支付模式CREDIT_PREAUTH_PAY
        response.put("auth_trade_pay_mode", "CREDIT_PREAUTH_PAY");

        return response;
    }

    /**
     * 获取值，为空则返回空的字符串
     *
     * @param content
     * @return
     */
    private static String getValue(Object content) {
        if (content == null) {
            return "";
        }

        if (content instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(content);
        } else {
            return String.valueOf(content);
        }
    }


}
