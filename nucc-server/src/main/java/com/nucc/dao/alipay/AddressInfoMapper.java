package com.nucc.dao.alipay;

import com.nucc.entity.alipay.AddressInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressInfoMapper {

    int deleteByPrimaryKey(@Param("merchantId") String merchantId);

    int insert(AddressInfo merchant);

    AddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AddressInfo merchant);

    List<AddressInfo> queryByKey(@Param("merchantId") String merchantId);
}