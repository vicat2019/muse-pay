package com.nucc.dao.alipay;

import com.nucc.entity.alipay.MerchantInfo;

public interface MerchantInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MerchantInfo merchantInfo);

    MerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(MerchantInfo merchantInfo);

    MerchantInfo getByMerchantId(String merchantId);

    MerchantInfo getByExternalId(String externalId);

    int getCountByExternalId(String externalId);

    int updateByExternalId(MerchantInfo merchantInfo);

}