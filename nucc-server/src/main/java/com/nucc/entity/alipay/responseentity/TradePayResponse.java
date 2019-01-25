package com.nucc.entity.alipay.responseentity;


import com.nucc.entity.alipay.TradeFundBill;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.TradeInfo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: muse-pay
 * @description: 网联阿里支付响应类
 * @author: Vincent
 * @create: 2018-11-26 15:37
 **/
public class TradePayResponse extends BaseResponseEntity {

    // 网联交易号
    private String trade_no;

    // 商户订单号,32 个字符以内、可包含字母、 数字、 下划线；需保证在商户端不重复。
    private String out_trade_no;

    // 买家支付宝账号
    private String buyer_logon_id;

    // 交易状态：
    // WAIT_BUYER_PAY（交易创建，等待买家付款）
    // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
    // TRADE_SUCCESS（交易支付成功）
    // TRADE_FINISHED（交易结束，不可退款）
    private String trade_status;

    // 交易金额
    private BigDecimal total_amount;

    // 实收金额
    private BigDecimal receipt_amount;

    // 交易支付使用的资金渠道， 详见下文说明。
    private TradeFundBill[] fund_billList;

    // 买家在支付宝的用户 id
    private String buyer_user_id;

    private Date gmt_payment;


    private BigDecimal settle_amount;
    private String pay_currency;
    private BigDecimal pay_amount;
    private String settle_trans_rate;
    private String trans_pay_rate;
    private String trans_currency;
    private String settle_currency;
    private BigDecimal buyer_pay_amount;
    private BigDecimal point_amount;
    private BigDecimal invoice_amount;
    private String store_name;
    private String auth_trade_pay_mode;
    private String business_params;
    private String buyer_user_type;
    private String mdiscount_amount;
    private String discount_amount;


    /**
     * 获取实例
     *
     * @return _nucc_alipay_trade_pay_response
     */
    public static TradePayResponse getInstance() {
        return new TradePayResponse();
    }

    /**
     * 生成返回内容
     * @param tradeInfo
     * @return
     */
    public static TradePayResponse getInstanceFrom(TradeInfo tradeInfo) {
        TradePayResponse response = new TradePayResponse();
        response.setTrade_no(tradeInfo.getTrade_no());
        response.setTotal_amount(tradeInfo.getTotal_amount());
        response.setBuyer_logon_id(tradeInfo.getBuyer_logon_id());
        response.setFund_billList(tradeInfo.getFund_bill_list());
        response.setReceipt_amount(tradeInfo.getReceipt_amount());
        response.setBuyer_user_id(tradeInfo.getBuyer_user_id());
        response.setTrade_status("WAIT_BUYER_PAY");
        response.setOut_trade_no(tradeInfo.getOut_trade_no());
        response.setFund_billList(getDefaultFundBillList(tradeInfo.getTotal_amount()));

        response.setSettle_amount(tradeInfo.getSettle_amount());
        response.setPay_currency(tradeInfo.getPay_currency());
        response.setPay_amount(tradeInfo.getPay_amount());
        response.setSettle_trans_rate(tradeInfo.getTrans_pay_rate());
        response.setTrans_pay_rate(tradeInfo.getTrans_pay_rate());
        response.setTrans_currency(tradeInfo.getTrans_currency());
        response.setSettle_currency(tradeInfo.getSettle_currency());
        response.setBuyer_pay_amount(tradeInfo.getBuyer_pay_amount());
        response.setPoint_amount(tradeInfo.getPoint_amount());
        response.setInvoice_amount(tradeInfo.getInvoice_amount());
        response.setStore_name(tradeInfo.getStore_name());
        response.setBuyer_user_type(tradeInfo.getBuyer_user_type());
        response.setMdiscount_amount(tradeInfo.getMdiscount_amount());
        response.setDiscount_amount(tradeInfo.getDiscount_amount());

        return response;
    }

    /**
     * 获取默认的交易支付使用的资金渠道列表
     * @param amount
     * @return
     */
    public static TradeFundBill[] getDefaultFundBillList(BigDecimal amount) {
        TradeFundBill[] bills = new TradeFundBill[1];
        TradeFundBill bill = new TradeFundBill();
        bill.setAmount(amount);
        bill.setReal_amount(amount);
        bill.setFund_channel("ALIPAYACCOUNT");
        bills[0] = bill;

        return bills;
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

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(BigDecimal receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public TradeFundBill[] getFund_billList() {
        return fund_billList;
    }

    public void setFund_billList(TradeFundBill[] fund_billList) {
        this.fund_billList = fund_billList;
    }

    public String getBuyer_user_id() {
        return buyer_user_id;
    }

    public void setBuyer_user_id(String buyer_user_id) {
        this.buyer_user_id = buyer_user_id;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public Date getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(Date gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public BigDecimal getSettle_amount() {
        return settle_amount;
    }

    public void setSettle_amount(BigDecimal settle_amount) {
        this.settle_amount = settle_amount;
    }

    public String getPay_currency() {
        return pay_currency;
    }

    public void setPay_currency(String pay_currency) {
        this.pay_currency = pay_currency;
    }

    public BigDecimal getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(BigDecimal pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getSettle_trans_rate() {
        return settle_trans_rate;
    }

    public void setSettle_trans_rate(String settle_trans_rate) {
        this.settle_trans_rate = settle_trans_rate;
    }

    public String getTrans_pay_rate() {
        return trans_pay_rate;
    }

    public void setTrans_pay_rate(String trans_pay_rate) {
        this.trans_pay_rate = trans_pay_rate;
    }

    public String getTrans_currency() {
        return trans_currency;
    }

    public void setTrans_currency(String trans_currency) {
        this.trans_currency = trans_currency;
    }

    public String getSettle_currency() {
        return settle_currency;
    }

    public void setSettle_currency(String settle_currency) {
        this.settle_currency = settle_currency;
    }

    public BigDecimal getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(BigDecimal buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public BigDecimal getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(BigDecimal point_amount) {
        this.point_amount = point_amount;
    }

    public BigDecimal getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(BigDecimal invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAuth_trade_pay_mode() {
        return auth_trade_pay_mode;
    }

    public void setAuth_trade_pay_mode(String auth_trade_pay_mode) {
        this.auth_trade_pay_mode = auth_trade_pay_mode;
    }

    public String getBusiness_params() {
        return business_params;
    }

    public void setBusiness_params(String business_params) {
        this.business_params = business_params;
    }

    public String getBuyer_user_type() {
        return buyer_user_type;
    }

    public void setBuyer_user_type(String buyer_user_type) {
        this.buyer_user_type = buyer_user_type;
    }

    public String getMdiscount_amount() {
        return mdiscount_amount;
    }

    public void setMdiscount_amount(String mdiscount_amount) {
        this.mdiscount_amount = mdiscount_amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }
}
