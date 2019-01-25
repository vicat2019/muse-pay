package com.nucc.entity.weixin.responseentity;

import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXMerchantInfo;
import com.nucc.util.TextUtils;

/**
 * @program: muse-pay
 * @description: 微信商户进件相应类
 * @author: Vincent
 * @create: 2018-11-29 11:44
 **/
public class WXMerchantResponse extends WXBaseResponse {

    // 处理结果：SUCCESS/FAIL
    private String result_code;
    // 处理信息
    private String result_msg;


    // 商户号
    private String mch_id;
    // 商户识别码，微信支付分配的商户识别码
    private String sub_mch_id;
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;


    private String appid;

    private String sub_merchant_id;

    private String merchant_name;

    private String merchant_shortname;

    private String service_phone;

    private String contact;

    private String contact_phone;

    private String contact_email;

    private String business;

    private String channel_id;

    private String contact_wechatid_type;

    private String contact_wechatid;

    private String merchant_remark;


    /**
     * 生成成功返回对象
     *
     * @return
     */
    public static WXMerchantResponse getSuccessResult() {
        WXMerchantResponse response = new WXMerchantResponse();
        response.setSuccessValue();
        response.setResult_code(WXBaseResponse.RESULT_SUCCESS);
        response.setResult_msg("操作成功");
        return response;
    }

    /**
     * 生成异常返回对象
     *
     * @return
     */
    public static WXMerchantResponse getErrResult() {
        WXMerchantResponse response = new WXMerchantResponse();
        response.setErrValue();
        response.setResult_code(WXBaseResponse.RESULT_FAIL);
        response.setResult_msg("操作失败");
        return response;
    }

    /**
     * 设置返回内容
     *
     * @param merchantInfo
     */
    public void setReturnData(WXMerchantInfo merchantInfo) {
        if (merchantInfo == null) {
            return;
        }
        this.mch_id = merchantInfo.getMch_id();
        this.sub_mch_id = merchantInfo.getSub_merchant_id();
        this.nonce_str = TextUtils.getRandomStr(16);
    }

    /**
     * 设置所有属性值
     *
     * @param merchantInfo
     */
    public void setReturnAllData(WXMerchantInfo merchantInfo) {
        if (merchantInfo == null) {
            return;
        }

        setReturnData(merchantInfo);

        this.appid = merchantInfo.getAppid();
        this.sub_merchant_id = merchantInfo.getSub_merchant_id();
        this.merchant_name = merchantInfo.getMerchant_name();
        this.merchant_shortname = merchantInfo.getMerchant_shortname();
        this.service_phone = merchantInfo.getService_phone();
        this.contact = merchantInfo.getContact();
        this.contact_phone = merchantInfo.getContact_phone();
        this.contact_email = merchantInfo.getContact_email();
        this.business = merchantInfo.getBusiness();
        this.channel_id = merchantInfo.getChannel_id();
        this.contact_wechatid_type = merchantInfo.getContact_wechatid_type();
        this.contact_wechatid = merchantInfo.getContact_wechatid();
        this.merchant_remark = merchantInfo.getMerchant_remark();
    }

    @Override
    public String toString() {
        return "WXMerchantResponse{" +
                "result_code='" + result_code + '\'' +
                ", result_msg='" + result_msg + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", sub_mch_id='" + sub_mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", appid='" + appid + '\'' +
                ", sub_merchant_id='" + sub_merchant_id + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", merchant_shortname='" + merchant_shortname + '\'' +
                ", service_phone='" + service_phone + '\'' +
                ", contact='" + contact + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", contact_email='" + contact_email + '\'' +
                ", business='" + business + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", contact_wechatid_type='" + contact_wechatid_type + '\'' +
                ", contact_wechatid='" + contact_wechatid + '\'' +
                ", merchant_remark='" + merchant_remark + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
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

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_shortname() {
        return merchant_shortname;
    }

    public void setMerchant_shortname(String merchant_shortname) {
        this.merchant_shortname = merchant_shortname;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getContact_wechatid_type() {
        return contact_wechatid_type;
    }

    public void setContact_wechatid_type(String contact_wechatid_type) {
        this.contact_wechatid_type = contact_wechatid_type;
    }

    public String getContact_wechatid() {
        return contact_wechatid;
    }

    public void setContact_wechatid(String contact_wechatid) {
        this.contact_wechatid = contact_wechatid;
    }

    public String getMerchant_remark() {
        return merchant_remark;
    }

    public void setMerchant_remark(String merchant_remark) {
        this.merchant_remark = merchant_remark;
    }
}
