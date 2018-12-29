package com.nucc.service.alipay;

import com.muse.common.entity.ResultData;
import com.nucc.entity.alipay.BankCardInfo;

public interface BankCardInfoService {

    ResultData add(BankCardInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(BankCardInfo obj) throws Exception;

    ResultData queryBankCardByKey(String merchantId) throws Exception;

}
