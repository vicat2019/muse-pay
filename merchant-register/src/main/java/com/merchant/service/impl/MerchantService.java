package com.merchant.service.impl;

import com.merchant.entity.RuiShengUserInfo;
import com.muse.common.entity.ResultData;

/**
 * @Description: 商户服务接口类
 * @Author: Vincent
 * @Date: 2019/2/12
 */
public interface MerchantService {

    ResultData add(RuiShengUserInfo userInfo) throws Exception;

    ResultData insert(RuiShengUserInfo userInfo) throws Exception;

    ResultData modifyRate(int rate, String mchId, String submchId) throws Exception;

}
