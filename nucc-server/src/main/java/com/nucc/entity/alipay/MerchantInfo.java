package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.nucc.entity.alipay.requestentity.MerchantInfoVO;

/**
 * @program: muse-pay
 * @description: 蚂蚁商户信息类
 * @author: Vincent
 * @create: 2018-11-27 09:23
 **/
public class MerchantInfo extends BaseEntityInfo {

    // 商户号
    private String sub_merchant_id;

    // 商户编号，有机构定义，需要保证在机构下唯一
    private String external_id;

    // 间连商户等级
    private String indeirect_level;

    // 商户名称
    private String name;

    // 商户简称
    private String alias_name;

    // 商户客服电话
    private String service_phone;

    // 商户经营类目
    private String category_id;

    // 商户来源机构标识，填写机构在支付宝的pid
    private String source;

    // 商户证件编号
    private String business_license;

    // 商户证件类型
    private String business_license_type;

    // 商户联系人信息
    private ContactInfo[] contact_info;

    // 商户地址信息
    private AddressInfo[] address_info;

    // 商户对银行所开立的结算卡信息
    private BankCardInfo[] bankcard_info;

    // 商户的支付二维码中的信息，用于营销活动
    private String[] pay_code_info;

    // 商户的支付宝账号
    private String[] logon_id;

    // 商户备注信息，可写额外信息
    private String memo;

    /**
     * 根据参数生成对象
     *
     * @param params
     * @return
     */
    public static MerchantInfo getInstance(MerchantInfoVO params) {
        MerchantInfo merchantInfo = new MerchantInfo();

        merchantInfo.setExternal_id(params.getExternal_id());
        merchantInfo.setAddress_info(params.getAddress_info());
        merchantInfo.setAlias_name(params.getAlias_name());
        merchantInfo.setBankcard_info(params.getBankcard_info());
        merchantInfo.setBusiness_license(params.getBusiness_license());
        merchantInfo.setCategory_id(params.getCategory_id());
        merchantInfo.setContact_info(params.getContact_infos());
        merchantInfo.setLogon_id(params.getLogon_id());
        merchantInfo.setMemo(params.getMemo());
        merchantInfo.setPay_code_info(params.getPay_code_info());
        merchantInfo.setService_phone(params.getService_phone());
        merchantInfo.setSource(params.getSource());

        return merchantInfo;
    }


    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(String business_license) {
        this.business_license = business_license;
    }

    public String getBusiness_license_type() {
        return business_license_type;
    }

    public void setBusiness_license_type(String business_license_type) {
        this.business_license_type = business_license_type;
    }

    public ContactInfo[] getContact_info() {
        return contact_info;
    }

    public void setContact_info(ContactInfo[] contact_info) {
        this.contact_info = contact_info;
    }

    public AddressInfo[] getAddress_info() {
        return address_info;
    }

    public void setAddress_info(AddressInfo[] address_info) {
        this.address_info = address_info;
    }

    public BankCardInfo[] getBankcard_info() {
        return bankcard_info;
    }

    public void setBankcard_info(BankCardInfo[] bankcard_info) {
        this.bankcard_info = bankcard_info;
    }

    public String[] getPay_code_info() {
        return pay_code_info;
    }

    public void setPay_code_info(String[] pay_code_info) {
        this.pay_code_info = pay_code_info;
    }

    public String[] getLogon_id() {
        return logon_id;
    }

    public void setLogon_id(String[] logon_id) {
        this.logon_id = logon_id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndeirect_level() {
        return indeirect_level;
    }

    public void setIndeirect_level(String indeirect_level) {
        this.indeirect_level = indeirect_level;
    }
}
