package com.nucc.entity.alipay.requestentity;

import com.nucc.entity.alipay.ExtendParams;
import com.nucc.entity.alipay.GoodDetail;
import com.nucc.entity.alipay.SubMerchant;
import com.nucc.entity.alipay.base.BaseRequestEntity;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @program: muse-pay
 * @description: 统一收单交易支付参数
 * @author: Vincent
 * @create: 2018-11-26 15:05
 **/
public class TradeInfoVO extends BaseRequestEntity {

    private String trade_no;
    // 商户订单号
    private String out_trade_no;

    // 交易发往的网联条码支付IDC标识
    private String idc_flag;
    // 支付场景[bar_code]
    private String scene;

    // 支付授权码
    private String auth_code;
    // 订单标题
    private String subject;
    // 产品码
    private String product_code;
    // 买家的支付宝用户ID，为空，则从输入的码值信息中获取
    private String buyer_id;
    // 如果为空，则默认为商户签约账户对应的支付宝用户ID
    private String seller_id;
    // 订单总金额[总金额 = 优惠计算的金额 + 不优惠计算的金额]
    private BigDecimal total_amount;
    // 参与优惠计算的金额
    private BigDecimal discountable_amount;
    // 不参与优惠计算的金额
    private BigDecimal undiscountable_amount;
    // 标价币种
    private String trans_currency;
    // 商户指定的结算币种
    private String settle_currency;
    // 订单描述
    private String body;
    // 订单包含的商品列表信息
    private GoodDetail[] goods_detail;
    // 商户操作员编号
    private String operator_id;
    // 商户门店编号
    private String store_id;
    // 商户机具终端编号
    private String terminal_id;
    // 业务扩展参数
    private ExtendParams extend_params;
    // 订单允许的最晚付款时间
    private String timeout_express;
    // 间连收商户信息题，支队特殊银行机构特定场景下使用此字段
    private SubMerchant sub_merchant;
    // 禁用支付渠道
    private String disable_pay_channels;
    // 商户的原始订单号
    private String merchant_order_no;
    // 预授权确认模式
    private String auth_confirm_mode;
    // 商户传入终端设备
    private String terminal_params;
    // 商户传入业务信息
    private String business_params;

    private String qr_code_timeout_express;
    private String buyer_logon_id;
    private String enable_pay_channels;


    //------------退款相关-----------------------------------------------------------------------------------------------
    // 需要退款的金额, 该金额不能大于订单金额, 单位为元, 支持两位小数
    private BigDecimal refund_amount;
    // 订单退款币种信息
    private String refund_currency;
    // 退款的原因说明
    private String refund_reason;
    // 标识一次退款请求， 同一笔交易多次退款需要保证唯一， 如需部分退款， 则此参数必
    private String out_request_no;
    // 存量交易时收单机构的pid，仅在存量交易退款时必填，非存量交易填空。默认为空
    private String org_pid;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getIdc_flag() {
        return idc_flag;
    }

    public void setIdc_flag(String idc_flag) {
        this.idc_flag = idc_flag;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    @Override
    public String toString() {
        return "TradeInfoVO{" +
                "trade_no='" + trade_no + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", idc_flag='" + idc_flag + '\'' +
                ", scene='" + scene + '\'' +
                ", auth_code='" + auth_code + '\'' +
                ", subject='" + subject + '\'' +
                ", product_code='" + product_code + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", total_amount=" + total_amount +
                ", discountable_amount=" + discountable_amount +
                ", undiscountable_amount=" + undiscountable_amount +
                ", trans_currency='" + trans_currency + '\'' +
                ", settle_currency='" + settle_currency + '\'' +
                ", body='" + body + '\'' +
                ", goods_detail=" + Arrays.toString(goods_detail) +
                ", operator_id='" + operator_id + '\'' +
                ", store_id='" + store_id + '\'' +
                ", terminal_id='" + terminal_id + '\'' +
                ", extend_params=" + extend_params +
                ", timeout_express='" + timeout_express + '\'' +
                ", sub_merchant=" + sub_merchant +
                ", disable_pay_channels='" + disable_pay_channels + '\'' +
                ", merchant_order_no='" + merchant_order_no + '\'' +
                ", auth_confirm_mode='" + auth_confirm_mode + '\'' +
                ", terminal_params='" + terminal_params + '\'' +
                ", business_params='" + business_params + '\'' +
                ", qr_code_timeout_express='" + qr_code_timeout_express + '\'' +
                ", buyer_logon_id='" + buyer_logon_id + '\'' +
                ", enable_pay_channels='" + enable_pay_channels + '\'' +
                ", refund_amount=" + refund_amount +
                ", refund_currency='" + refund_currency + '\'' +
                ", refund_reason='" + refund_reason + '\'' +
                ", out_request_no='" + out_request_no + '\'' +
                ", org_pid='" + org_pid + '\'' +
                ", pid='" + pid + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", charset='" + charset + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", version='" + version + '\'' +
                ", appAuth_toke='" + appAuth_toke + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", biz_content='" + biz_content + '\'' +
                '}';
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
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

    public BigDecimal getDiscountable_amount() {
        return discountable_amount;
    }

    public void setDiscountable_amount(BigDecimal discountable_amount) {
        this.discountable_amount = discountable_amount;
    }

    public BigDecimal getUndiscountable_amount() {
        return undiscountable_amount;
    }

    public void setUndiscountable_amount(BigDecimal undiscountable_amount) {
        this.undiscountable_amount = undiscountable_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public GoodDetail[] getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(GoodDetail[] goods_detail) {
        this.goods_detail = goods_detail;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public ExtendParams getExtend_params() {
        return extend_params;
    }

    public void setExtend_params(ExtendParams extend_params) {
        this.extend_params = extend_params;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public SubMerchant getSub_merchant() {
        return sub_merchant;
    }

    public void setSub_merchant(SubMerchant sub_merchant) {
        this.sub_merchant = sub_merchant;
    }

    public String getDisable_pay_channels() {
        return disable_pay_channels;
    }

    public void setDisable_pay_channels(String disable_pay_channels) {
        this.disable_pay_channels = disable_pay_channels;
    }

    public String getMerchant_order_no() {
        return merchant_order_no;
    }

    public void setMerchant_order_no(String merchant_order_no) {
        this.merchant_order_no = merchant_order_no;
    }

    public String getAuth_confirm_mode() {
        return auth_confirm_mode;
    }

    public void setAuth_confirm_mode(String auth_confirm_mode) {
        this.auth_confirm_mode = auth_confirm_mode;
    }

    public String getTerminal_params() {
        return terminal_params;
    }

    public void setTerminal_params(String terminal_params) {
        this.terminal_params = terminal_params;
    }

    public String getBusiness_params() {
        return business_params;
    }

    public void setBusiness_params(String business_params) {
        this.business_params = business_params;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getEnable_pay_channels() {
        return enable_pay_channels;
    }

    public void setEnable_pay_channels(String enable_pay_channels) {
        this.enable_pay_channels = enable_pay_channels;
    }

    public String getQr_code_timeout_express() {
        return qr_code_timeout_express;
    }

    public void setQr_code_timeout_express(String qr_code_timeout_express) {
        this.qr_code_timeout_express = qr_code_timeout_express;
    }

    public BigDecimal getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(BigDecimal refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_currency() {
        return refund_currency;
    }

    public void setRefund_currency(String refund_currency) {
        this.refund_currency = refund_currency;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public String getOrg_pid() {
        return org_pid;
    }

    public void setOrg_pid(String org_pid) {
        this.org_pid = org_pid;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }
}
