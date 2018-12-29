package com.nucc.entity.alipay.responseentity;

import com.nucc.entity.alipay.TradeFundBill;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.TradeInfo;
import com.nucc.entity.alipay.RefundInfo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: muse-pay
 * @description: 退款返回结果
 * @author: Vincent
 * @create: 2018-11-26 18:38
 **/
public class TradeRefundResponse extends BaseResponseEntity {

    // 网联交易号
    private String trade_no;

    // 原支付订单的商户订单号，32个字符以内、可包含字母、数字、下划线；需保证在商户端不重复。
    private String out_trade_no;

    // 买家支付宝账号
    private String buyer_logon_id;

    // 本次退款是否发生了资金变化
    private String fund_change = "N";

    // 退款总金额
    private BigDecimal refund_fee;

    // 退款币种信息
    private String refund_currency = "CNY";

    // 退款支付时间
    private Date gmt_refund_pay;

    // 买家在支付宝的用户 id
    private String buyer_user_id;



    // 退款使用的资金渠道
    private TradeFundBill[] refund_detail_item_list;

    // 交易在支付时候的门店名称
    private String store_name;

    // 本次退款金额中买家退款金额
    private String present_refund_buyer_amount;

    // 本次退款金额中平台优惠退款金额
    private String present_refund_discount_amount;

    // 本次退款金额中商家优惠退款金额
    private String present_refund_mdiscount_amount;




    // 本笔退款对应的退款请求号
    private String out_request_no;

    // 该笔退款所对应的交易的订单金额
    private BigDecimal total_amount;

    // 行业特殊信息（例如在医保卡支付退款中， 医保局向商户返回医疗信息）
    private String industry_sepc_detail;



    private String refund_reason;
    private BigDecimal refund_amount;


    /**
     * 获取提交退款申请的返回内容
     * @param refundInfo
     * @param tradeInfo
     * @return
     */
    public static TradeRefundResponse getInstanceFrom(RefundInfo refundInfo, TradeInfo tradeInfo) {
        TradeRefundResponse response = new TradeRefundResponse();

        response.setTrade_no(refundInfo.getTrade_no());
        response.setOut_trade_no(refundInfo.getOut_trade_no());
        response.setBuyer_logon_id(tradeInfo.getBuyer_logon_id());
        response.setFund_change("N");
        response.setGmt_refund_pay(new Date());
        response.setRefund_fee(refundInfo.getTotal_amount());
        response.setRefund_currency("CNY");
        response.setBuyer_user_id(tradeInfo.getBuyer_user_id());

        return response;
    }

    /**
     * 获取查询退款请求的返回内容
     * @param refundInfo
     * @return
     */
    public static TradeRefundResponse getQueryResponse(RefundInfo refundInfo) {
        TradeRefundResponse response = new TradeRefundResponse();

        response.setTrade_no(refundInfo.getTrade_no());
        response.setOut_trade_no(refundInfo.getOut_trade_no());
        response.setRefund_reason(refundInfo.getRefund_reason());
        response.setTotal_amount(refundInfo.getTotal_amount());
        response.setRefund_amount(refundInfo.getRefund_amount());

        return response;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getFund_change() {
        return fund_change;
    }

    public void setFund_change(String fund_change) {
        this.fund_change = fund_change;
    }

    public BigDecimal getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(BigDecimal refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_currency() {
        return refund_currency;
    }

    public void setRefund_currency(String refund_currency) {
        this.refund_currency = refund_currency;
    }

    public Date getGmt_refund_pay() {
        return gmt_refund_pay;
    }

    public void setGmt_refund_pay(Date gmt_refund_pay) {
        this.gmt_refund_pay = gmt_refund_pay;
    }

    public TradeFundBill[] getRefund_detail_item_list() {
        return refund_detail_item_list;
    }

    public void setRefund_detail_item_list(TradeFundBill[] refund_detail_item_list) {
        this.refund_detail_item_list = refund_detail_item_list;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBuyer_user_id() {
        return buyer_user_id;
    }

    public void setBuyer_user_id(String buyer_user_id) {
        this.buyer_user_id = buyer_user_id;
    }

    public String getPresent_refund_buyer_amount() {
        return present_refund_buyer_amount;
    }

    public void setPresent_refund_buyer_amount(String present_refund_buyer_amount) {
        this.present_refund_buyer_amount = present_refund_buyer_amount;
    }

    public String getPresent_refund_discount_amount() {
        return present_refund_discount_amount;
    }

    public void setPresent_refund_discount_amount(String present_refund_discount_amount) {
        this.present_refund_discount_amount = present_refund_discount_amount;
    }

    public String getPresent_refund_mdiscount_amount() {
        return present_refund_mdiscount_amount;
    }

    public void setPresent_refund_mdiscount_amount(String present_refund_mdiscount_amount) {
        this.present_refund_mdiscount_amount = present_refund_mdiscount_amount;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public String getIndustry_sepc_detail() {
        return industry_sepc_detail;
    }

    public void setIndustry_sepc_detail(String industry_sepc_detail) {
        this.industry_sepc_detail = industry_sepc_detail;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public BigDecimal getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(BigDecimal refund_amount) {
        this.refund_amount = refund_amount;
    }
}
