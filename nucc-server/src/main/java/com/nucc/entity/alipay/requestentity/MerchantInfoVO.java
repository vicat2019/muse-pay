package com.nucc.entity.alipay.requestentity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nucc.entity.alipay.AddressInfo;
import com.nucc.entity.alipay.BankCardInfo;
import com.nucc.entity.alipay.ContactInfo;
import com.nucc.entity.alipay.MerchantInfo;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 商户入驻参数类
 * @author: Vincent
 * @create: 2018-11-27 10:14
 **/
public class MerchantInfoVO implements Serializable {
    private Logger log = LoggerFactory.getLogger("MerchantInfoVO");

    private String sub_merchant_id;

    // 商户编号，有机构定义，需要保证在机构下唯一
    private String external_id;

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
    private String contact_info;
    private ContactInfo[] contact_infos;

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
     * 检查参数是否合法
     *
     * @return
     */
    public BaseResponseEntity paramLegal() {
        if (StringUtils.isEmpty(this.external_id)) {
            return BaseResponseEntity.getErrResult("商户编号为空");
        }
        if (StringUtils.isEmpty(this.name)) {
            return BaseResponseEntity.getErrResult("名称为空");
        }
        if (StringUtils.isEmpty(this.alias_name)) {
            return BaseResponseEntity.getErrResult("商户简称为空");
        }
        if (StringUtils.isEmpty(this.service_phone)) {
            return BaseResponseEntity.getErrResult("客服电话为空");
        }
        if (StringUtils.isEmpty(this.category_id)) {
            return BaseResponseEntity.getErrResult("经营类别为空");
        }
        if (StringUtils.isEmpty(this.source)) {
            return BaseResponseEntity.getErrResult("来源为空");
        }
        /*if (this.contact_infos == null || this.contact_infos.length == 0) {
            return BaseResponseEntity.getErrResult("联系方式为空");
        }*/
        return BaseResponseEntity.getSuccessResult();
    }

    /**
     * 获取商户对象
     *
     * @return
     */
    public MerchantInfo getMerchant() {
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setSub_merchant_id(this.sub_merchant_id);
        merchantInfo.setExternal_id(this.external_id);
        merchantInfo.setAddress_info(this.address_info);
        merchantInfo.setAlias_name(this.alias_name);
        merchantInfo.setBankcard_info(this.bankcard_info);
        merchantInfo.setBusiness_license(this.business_license);
        merchantInfo.setBusiness_license_type(this.business_license_type);
        merchantInfo.setCategory_id(this.category_id);
        merchantInfo.setAlias_name(this.alias_name);
        merchantInfo.setContact_info(getContact_infos());
        merchantInfo.setLogon_id(this.logon_id);
        merchantInfo.setMemo(this.memo);
        merchantInfo.setPay_code_info(this.pay_code_info);
        merchantInfo.setSource(this.source);
        merchantInfo.setService_phone(this.service_phone);
        merchantInfo.setName(this.name);

        return merchantInfo;
    }


    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ContactInfo[] getContact_infos() {
        log.info("解析联系人参数=" + this.contact_info);

        try {
            JSONArray jsonArray = JSONArray.parseArray(this.contact_info);
            log.info("解析成JSON数组=" + (jsonArray!=null?jsonArray.toString():"null"));

            if (jsonArray != null && jsonArray.size() > 0) {
                ContactInfo[] temp = new ContactInfo[jsonArray.size()];

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSON item = (JSON) jsonArray.get(i);
                    temp[i] = JSON.parseObject(item.toString(), ContactInfo.class);
                }
                return temp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析联系人信息异常=" + e.getMessage());
        }

        return contact_infos;
    }

    public void setContact_infos(ContactInfo[] contact_infos) {
        this.contact_infos = contact_infos;
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

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }
}
