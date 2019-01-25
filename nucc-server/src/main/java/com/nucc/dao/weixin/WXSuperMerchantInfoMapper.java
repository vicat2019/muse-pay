package com.nucc.dao.weixin;

import com.nucc.entity.weixin.WXSuperMerchantInfo;

public interface WXSuperMerchantInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WXSuperMerchantInfo record);

    WXSuperMerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(WXSuperMerchantInfo record);

    WXSuperMerchantInfo getMerchantByAppId(String appId);
}