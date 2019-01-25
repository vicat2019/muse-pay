package com.nucc.entity.weixin.requestentity;

import com.nucc.entity.weixin.WXMerchantInfo;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 微信商户进件参数类
 * @author: Vincent
 * @create: 2018-11-29 11:26
 **/
public class WXMerchantInfoVO implements Serializable {

    // 银行服务商的公众号ID
    private String appid;
    // 银行服务商的商户号
    private String mch_id;
    // 签名
    private String sign;
    // 商户名称
    private String merchant_name;
    // 商户简称
    private String merchant_shortname;
    // 客服电话
    private String service_phone;
    // 渠道号-银行为其渠道商申请（在服务商平台申请，请见《渠道录入指引》）的渠道标识.
    // 银行直连商户也需要单独申请渠道号
    private String channel_id;
    // 经营类目
    private String business;
    // 商户备注
    private String merchant_remark;


    // 联系人名称
    private String contact;
    // 联系电话
    private String contact_phone;
    // 联系邮箱
    private String contact_email;
    // 联系人微信账号类型 - 如传微信号，值为type_wechatid，如果“收款人微信账号”要传openid,值为type_openid
    private String contact_wechatid_type;
    // 联系人微信账号-微信号：打开微信，在"个人信息"中查看到的"微信号"
    // openid：需在服务商appid下通过OAUTH2.0接口获取用户的openid，此处传openid的具体值
    private String contact_wechatid;

    // 子商户号
    private String sub_mch_id;


    /**
     * 获取商户信息对象
     * @return
     */
    public WXMerchantInfo getMerchantInfo() {
        WXMerchantInfo merchantInfo = new WXMerchantInfo();

        merchantInfo.setAppid(this.appid);
        merchantInfo.setMch_id(this.mch_id);
        merchantInfo.setSub_merchant_id(this.sub_mch_id);
        merchantInfo.setBusiness(this.business);
        merchantInfo.setChannel_id(this.channel_id);
        merchantInfo.setContact(this.contact);
        merchantInfo.setContact_email(this.contact_email);
        merchantInfo.setContact_phone(this.contact_phone);
        merchantInfo.setContact_wechatid(this.contact_wechatid);
        merchantInfo.setContact_wechatid_type(this.contact_wechatid_type);
        merchantInfo.setMerchant_name(this.merchant_name);
        merchantInfo.setMerchant_remark(this.merchant_remark);
        merchantInfo.setMerchant_shortname(this.merchant_shortname);
        merchantInfo.setService_phone(this.service_phone);

        return merchantInfo;
    }


    @Override
    public String toString() {
        return "WXMerchantInfoVO{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", sign='" + sign + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", merchant_shortname='" + merchant_shortname + '\'' +
                ", service_phone='" + service_phone + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", business='" + business + '\'' +
                ", merchant_remark='" + merchant_remark + '\'' +
                ", contact='" + contact + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", contact_email='" + contact_email + '\'' +
                ", contact_wechatid_type='" + contact_wechatid_type + '\'' +
                ", contact_wechatid='" + contact_wechatid + '\'' +
                '}';
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getMerchant_remark() {
        return merchant_remark;
    }

    public void setMerchant_remark(String merchant_remark) {
        this.merchant_remark = merchant_remark;
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

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }
}
