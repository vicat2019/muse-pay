package com.nucc.service.alipay;

import com.muse.common.entity.ResultData;
import com.nucc.entity.alipay.AddressInfo;

public interface AddressInfoService {

    ResultData add(AddressInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(AddressInfo obj) throws Exception;

    ResultData queryAddressByKey(String merchantId) throws Exception;

}
