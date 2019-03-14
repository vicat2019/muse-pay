package com.merchant.dao;


import com.merchant.entity.BankCardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankCardInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BankCardInfo record);

    BankCardInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BankCardInfo record);

    List<BankCardInfo> getAvailableBankCard();

    List<BankCardInfo> getAllBankCardByChannel(@Param("mchId") String mchId);

    int updateRegInfo(@Param("registerCount") int registerCount, @Param("status") String status,
                      @Param("id") int id);

    List<BankCardInfo> getAllBankCard();

    int insertByBatch(List<BankCardInfo> list);


}