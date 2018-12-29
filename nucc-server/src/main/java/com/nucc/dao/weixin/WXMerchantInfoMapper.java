package com.nucc.dao.weixin;


import com.nucc.entity.weixin.WXMerchantInfo;

public interface WXMerchantInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WXMerchantInfo record);

    WXMerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(WXMerchantInfo record);

    Integer getCountBy(WXMerchantInfo merchantInfo);

    Integer getCountBySubMerchantId(String subMerchantId);

    Integer updateBySubMerchantId(WXMerchantInfo merchantInfo);

    WXMerchantInfo query(WXMerchantInfo merchantInfo);
}