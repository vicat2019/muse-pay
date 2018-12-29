package com.nucc.dao.alipay;

import com.nucc.entity.alipay.BankCardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankCardInfoMapper {

    int deleteByPrimaryKey(@Param("merchantId") String merchantId);

    int insert(BankCardInfo merchant);

    BankCardInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BankCardInfo merchant);

    List<BankCardInfo> queryByKey(@Param("merchantId") String merchantId);
}