package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: muse-pay
 * @description: 订单信息
 * @author: Vincent
 * @create: 2018-11-27 17:02
 **/
public class TradeInfo extends BaseEntityInfo {

    private String trade_no;

    // 商户订单号
    private String out_trade_no;

    // 买家支付宝账号
    private String buyer_logon_id;

    // 结算币种订单金额
    private BigDecimal settle_amount;

    // 支付币种
    private String pay_currency;

    // 支付币种订单金额
    private BigDecimal pay_amount;

    // 订单总金额
    private BigDecimal total_amount;

    // 实收金额
    private BigDecimal receipt_amount;

    // 标价币种兑换支付币种汇率
    private String trans_pay_rate;

    // 标价币种
    private String trans_currency;

    // 商户指定的结算币种
    private String settle_currency;

    // 买家付款金额
    private BigDecimal buyer_pay_amount;

    // 使用集分宝付款金额
    private BigDecimal point_amount;

    // 可给用户开具的发票金额
    private BigDecimal invoice_amount;

    // 发生支付交易的商户门店名称
    private String store_name;

    // 买家用户类型
    private String buyer_user_type;

    // 上家优惠金额
    private String mdiscount_amount;

    // 平台优惠金额
    private String discount_amount;

    // 买家在支付宝的用户ID
    private String buyer_user_id;

    // 交易状态
    private String trade_status;

    // 交易支付使用的资金渠道
    private TradeFundBill[] fund_bill_list;

    // 支付时间
    private Date gmt_payment;

    private String trade_type;


    private String pid;
    private String out_biz_no;
    private String subject;
    private String body;
    private Date gmt_close;
    private String seller_id;
    private String seller_email;
    private String notify_url;


    /**
     * 生成交易信息对象
     * @param tradeInfoVO
     * @return
     */
    public static TradeInfo getInstanceFrom(TradeInfoVO tradeInfoVO) {
        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setPid(tradeInfoVO.getPid());
        tradeInfo.setOut_trade_no(tradeInfoVO.getOut_trade_no());
        tradeInfo.setBuyer_logon_id("");
        tradeInfo.setTotal_amount(tradeInfoVO.getTotal_amount());
        tradeInfo.setReceipt_amount(BigDecimal.valueOf(0));
        tradeInfo.setBuyer_user_id(tradeInfoVO.getBuyer_id());

        tradeInfo.setSettle_amount(tradeInfoVO.getTotal_amount());
        tradeInfo.setPay_amount(tradeInfoVO.getTotal_amount());
        tradeInfo.setPay_currency("CNY");
        tradeInfo.setTrans_pay_rate("1");
        tradeInfo.setTrans_currency("CNY");
        tradeInfo.setSettle_currency("CNY");
        tradeInfo.setBuyer_pay_amount(tradeInfoVO.getTotal_amount());
        tradeInfo.setPoint_amount(BigDecimal.valueOf(0));
        tradeInfo.setInvoice_amount(tradeInfoVO.getTotal_amount());
        tradeInfo.setStore_name("");
        tradeInfo.setBuyer_user_type("PRIVATE");
        tradeInfo.setMdiscount_amount("0");
        tradeInfo.setDiscount_amount("0");

        tradeInfo.setBody(tradeInfoVO.getBody());
        tradeInfo.setSubject(tradeInfoVO.getSubject());
        tradeInfo.setSeller_id(tradeInfoVO.getSeller_id());
        tradeInfo.setSeller_email("");
        tradeInfo.setNotify_url(tradeInfoVO.getNotify_url());

        return tradeInfo;
    }

    /**
     * 检查参数是否合法
     * @return
     */
    public BaseResponseEntity legalParam() {
        if (StringUtils.isEmpty(out_trade_no)) {
            return BaseResponseEntity.getErrResult("商户订单号不能为空");
        }
        if (total_amount==null) {
            return BaseResponseEntity.getErrResult("交易总金额不能为空");
        }

        return BaseResponseEntity.getSuccessResult();
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

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
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

    public TradeFundBill[] getFund_bill_list() {
        return fund_bill_list;
    }

    public void setFund_bill_list(TradeFundBill[] fund_bill_list) {
        this.fund_bill_list = fund_bill_list;
    }

    public String getBuyer_user_id() {
        return buyer_user_id;
    }

    public void setBuyer_user_id(String buyer_user_id) {
        this.buyer_user_id = buyer_user_id;
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

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(Date gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
