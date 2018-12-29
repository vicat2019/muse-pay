package com.nucc.service.alipay;

import com.muse.common.entity.ResultData;
import com.nucc.entity.alipay.ContactInfo;

public interface ContactInfoService {

    ResultData add(ContactInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ContactInfo obj) throws Exception;

    ResultData queryContactByKey(String merchantId) throws Exception;

    ResultData delByMerchantId(String merchantId) throws Exception;

}
