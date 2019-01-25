package com.nucc.entity.weixin.responseentity;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXRefundInfo;
import com.nucc.entity.weixin.WXTradeInfo;
import com.nucc.util.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 支付返回类
 * @author: Vincent
 * @create: 2018-12-02 15:10
 **/
public class WXTradeResponse extends WXBaseResponse {

    // 公共参数
    private String appid;
    private String sub_appid;
    private String mch_id;
    private String sub_mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;


    // 刷卡返回参数
    private String trade_type;
    private String bank_type;
    private int cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;


    // 查询返回参数
    private String trade_status;
    private int total_fee;
    private String trade_state_desc;


    // 是否需要继续调用撤销
    private String recall;


    // 预下单返回参数
    private String perpay_id;
    private String code_url;


    // 退款返回参数
    private String out_refund_no;
    private String refund_id;
    private int refund_fee;
    private String refund_fee_type;
    private int cash_refund_fee;
    private int coupon_refund_fee;


    // 查询单条退款返回参数
    private String fee_type;
    private String refund_status;
    // 退款进入账号
    private String refund_recv_account;
    private String refund_success_time;
    private String refund_create_time;


    // 查询所有退款返回参数
    private int settlement_total_fee;
    private String refunds;

    // 授权码查询
    private String openid;
    private String sub_openid;


    /**
     * 生成成功返回对象
     *
     * @return
     */
    public static WXTradeResponse getSuccessResult() {
        WXTradeResponse response = new WXTradeResponse();
        response.setSuccessValue();
        response.setResult_code(WXBaseResponse.RESULT_SUCCESS);
        return response;
    }

    /**
     * 生成异常返回对象
     *
     * @return
     */
    public static WXTradeResponse getErrResult() {
        WXTradeResponse response = new WXTradeResponse();
        response.setErrValue();
        response.setResult_code(WXBaseResponse.RESULT_FAIL);
        return response;
    }


    /**
     * 设置公共参数部分
     *
     * @param tradeInfo
     */
    public void setPublishResponse(WXTradeInfo tradeInfo) {
        if (tradeInfo == null) {
            return;
        }
        this.appid = tradeInfo.getAppid();
        this.sub_appid = tradeInfo.getSub_appid();
        this.mch_id = tradeInfo.getMch_id();
        this.sub_mch_id = tradeInfo.getSub_mch_id();
        this.device_info = tradeInfo.getDevice_info();
        this.nonce_str = TextUtils.getRandomStr(32);
        this.transaction_id = this.getTransaction_id();
    }

    /**
     * 设置刷卡返回内容
     *
     * @param tradeInfo
     */
    public void setMicroPayResponse(WXTradeInfo tradeInfo) {
        // 设置公共部分
        setPublishResponse(tradeInfo);

        // 设置业务部分
        this.trade_type = tradeInfo.getTrade_type();
        this.bank_type = tradeInfo.getBank_type();
        this.cash_fee = tradeInfo.getCash_fee();
        this.transaction_id = tradeInfo.getTransaction_id();
        this.out_trade_no = tradeInfo.getOut_trade_no();
        this.time_end = tradeInfo.getTime_end();
        this.total_fee = tradeInfo.getTotal_fee();
    }

    /**
     * 设置查询返回内容
     *
     * @param tradeInfo
     */
    public void setTradeQueryResposnse(WXTradeInfo tradeInfo) {
        // 设置公共部分
        setPublishResponse(tradeInfo);

        // 设置业务部分
        this.device_info = tradeInfo.getDevice_info();
        this.trade_type = tradeInfo.getTrade_type();
        this.trade_status = tradeInfo.getTrade_status();
        this.bank_type = tradeInfo.getBank_type();
        this.cash_fee = tradeInfo.getCash_fee();
        this.transaction_id = tradeInfo.getTransaction_id();
        this.out_trade_no = tradeInfo.getOut_trade_no();
        this.time_end = tradeInfo.getTime_end();
        this.trade_state_desc = tradeInfo.getTrade_state_desc();
        this.total_fee = tradeInfo.getTotal_fee();
        this.settlement_total_fee = tradeInfo.getSettlement_total_fee();
    }


    /**
     * 设置撤销返回内容
     *
     * @param tradeInfo
     * @param isContinueCall
     */
    public void setCancelResponse(WXTradeInfo tradeInfo, String isContinueCall) {
        // 设置公共部分
        setPublishResponse(tradeInfo);
        this.recall = isContinueCall;
    }

    /**
     * 设置撤销返回内容
     *
     * @param tradeInfo
     */
    public void setUnifiedResponse(WXTradeInfo tradeInfo, String code_url) {
        // 设置公共部分
        setPublishResponse(tradeInfo);

        this.perpay_id = tradeInfo.getPerpay_id();
        this.code_url = code_url;
    }


    /**
     * 设置申请退款返回内容
     *
     * @param refundInfo
     */
    public void setRefundResponse(WXTradeInfo tradeInfo, WXRefundInfo refundInfo) {
        setPublishResponse(tradeInfo);

        this.out_refund_no = refundInfo.getOut_refund_no();
        this.refund_id = refundInfo.getRefund_id();
        this.refund_fee = refundInfo.getRefund_fee();
        this.refund_fee_type = refundInfo.getRefund_fee_type();
    }

    /**
     * 设置退款查询返回内容
     *
     * @param tradeInfo
     * @param refundInfo
     */
    public void setRefundQueryResponse(WXTradeInfo tradeInfo, WXRefundInfo refundInfo) {
        setPublishResponse(tradeInfo);

        this.fee_type = refundInfo.getRefund_fee_type();
        this.cash_fee = refundInfo.getCash_fee();
        this.out_refund_no = refundInfo.getOut_refund_no();
        this.refund_id = refundInfo.getRefund_id();
        this.refund_fee = refundInfo.getRefund_fee();
        this.refund_status = refundInfo.getRefund_status();
        this.refund_recv_account = refundInfo.getRefund_recv_account();
    }

    /**
     * 设置查询所有退款返回内容
     *
     * @param tradeInfo
     * @param refundList
     */
    public void setTradeQueryAllResponse(WXTradeInfo tradeInfo, List<WXRefundInfo> refundList) {
        setPublishResponse(tradeInfo);

        total_fee = 0;
        cash_fee = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (WXRefundInfo item : refundList) {
            total_fee += item.getRefund_fee();
            cash_fee += item.getCash_fee();

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getRefund_id());
            itemMap.put("out_refund_no", item.getOut_refund_no());
            itemMap.put("status", item.getRefund_status());

            Map<String, Object> subItemMap = new HashMap<>();
            subItemMap.put("settlement_refund", item.getSettlement_refund());
            subItemMap.put("refund", item.getRefund_fee());
            subItemMap.put("payer_refund", item.getRefund_fee());
            itemMap.put("amount", subItemMap);

            result.add(itemMap);
        }

        refunds = JSON.toJSON(result).toString();
    }

    /**
     * 设置授权码查询OpenId返回结果
     *
     * @param tradeInfo
     */
    public void setAuthCodeToOpenIdResponse(WXTradeInfo tradeInfo) {
        setPublishResponse(tradeInfo);

        this.openid = TextUtils.getRandomStr(28);
        this.sub_openid = TextUtils.getRandomStr(28);
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall;
    }

    public String getPerpay_id() {
        return perpay_id;
    }

    public void setPerpay_id(String perpay_id) {
        this.perpay_id = perpay_id;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public void setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
    }

    public int getCash_refund_fee() {
        return cash_refund_fee;
    }

    public void setCash_refund_fee(int cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
    }

    public int getCoupon_refund_fee() {
        return coupon_refund_fee;
    }

    public void setCoupon_refund_fee(int coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getRefund_recv_account() {
        return refund_recv_account;
    }

    public void setRefund_recv_account(String refund_recv_account) {
        this.refund_recv_account = refund_recv_account;
    }

    public String getRefund_success_time() {
        return refund_success_time;
    }

    public void setRefund_success_time(String refund_success_time) {
        this.refund_success_time = refund_success_time;
    }

    public String getRefund_create_time() {
        return refund_create_time;
    }

    public void setRefund_create_time(String refund_create_time) {
        this.refund_create_time = refund_create_time;
    }
}
