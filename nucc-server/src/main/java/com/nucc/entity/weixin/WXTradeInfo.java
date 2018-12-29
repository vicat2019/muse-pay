package com.nucc.entity.weixin;

import com.muse.common.entity.BaseEntityInfo;

public class WXTradeInfo extends BaseEntityInfo {

    private String appid;

    private String sub_appid;

    private String mch_id;

    private String sub_mch_id;

    private String channel_id;

    private String device_info;


    private String transaction_id;

    private String out_trade_no;

    private String body;

    private String attach;

    private int total_fee;

    private String fee_type;

    private String spbill_create_ip;

    private String goods_tag;

    private String limit_pay;

    private Boolean need_receipt;

    private String trade_type;

    private String bank_type;

    private int coupon_fee;

    private int settlement_total_fee;

    private String time_end;

    private String trade_state_desc;

    private int cash_fee;

    private String cash_fee_type;

    private String notify_url;

    private String trade_status;

    private String perpay_id;


    private String auth_code;

    @Override
    public String toString() {
        return "WXTradeInfo{" +
                "appid='" + appid + '\'' +
                ", sub_appid='" + sub_appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", sub_mch_id='" + sub_mch_id + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", device_info='" + device_info + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", body='" + body + '\'' +
                ", attach='" + attach + '\'' +
                ", total_fee=" + total_fee +
                ", fee_type='" + fee_type + '\'' +
                ", spbill_create_ip='" + spbill_create_ip + '\'' +
                ", goods_tag='" + goods_tag + '\'' +
                ", limit_pay='" + limit_pay + '\'' +
                ", need_receipt=" + need_receipt +
                ", trade_type='" + trade_type + '\'' +
                ", bank_type='" + bank_type + '\'' +
                ", coupon_fee=" + coupon_fee +
                ", settlement_total_fee=" + settlement_total_fee +
                ", time_end='" + time_end + '\'' +
                ", trade_state_desc='" + trade_state_desc + '\'' +
                ", cash_fee=" + cash_fee +
                ", cash_fee_type='" + cash_fee_type + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", trade_status='" + trade_status + '\'' +
                ", perpay_id='" + perpay_id + '\'' +
                ", auth_code='" + auth_code + '\'' +
                '}';
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public Boolean getNeed_receipt() {
        return need_receipt;
    }

    public void setNeed_receipt(Boolean need_receipt) {
        this.need_receipt = need_receipt;
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

    public int getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(int coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public int getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(int settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getPerpay_id() {
        return perpay_id;
    }

    public void setPerpay_id(String perpay_id) {
        this.perpay_id = perpay_id;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }
}