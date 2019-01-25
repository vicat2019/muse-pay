package com.muse.pay.dao;

import com.muse.pay.entity.MuseOrderInfo;
import org.apache.ibatis.annotations.Param;

public interface MuseOrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MuseOrderInfo record);

    int insertSelective(MuseOrderInfo record);

    MuseOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MuseOrderInfo record);

    int updateByPrimaryKey(MuseOrderInfo record);

    MuseOrderInfo selectByOrderNo(@Param("orderNo") String orderNo);

    MuseOrderInfo selectByMerchantAndTradeNo(@Param("merchantNo") String merchantNo,
                                             @Param("orderNo") String orderNo);
}