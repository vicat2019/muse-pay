package com.nucc.service.alipay;

import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.MerchantInfoVO;


/**
 * Created by code generator  on 2018/09/26.
 */
public interface MerchantInfoService {

    BaseResponseEntity add(MerchantInfoVO obj) throws Exception;

    BaseResponseEntity del(Integer id) throws Exception;

    BaseResponseEntity update(MerchantInfoVO obj) throws Exception;

    BaseResponseEntity get(Integer id) throws Exception;

    BaseResponseEntity setStatus(Integer id, String status) throws Exception;

    BaseResponseEntity getRandomActiveMerchant() throws Exception;

    BaseResponseEntity queryMerchant(String name, String status, String payKey, String paySecret, int pageNum, int pageSize) throws Exception;

    BaseResponseEntity queryMerchantByKey(MerchantInfoVO params) throws Exception;

}
