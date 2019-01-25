package com.nucc.dao.alipay;

import com.nucc.entity.alipay.ContactInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactInfoMapper {

    int deleteByMerchantId(@Param("merchantId") String merchantId);

    int insert(ContactInfo merchant);

    ContactInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ContactInfo merchant);

    List<ContactInfo> selectByMerchantId(@Param("merchantId") String merchantId);
}