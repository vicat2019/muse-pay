package com.muse.pay.dao;

import com.muse.pay.entity.MerchantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantInfo merchant);

    MerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(MerchantInfo merchant);

    int setStatus(@Param("status") String status, @Param("id") int id);

    List<MerchantInfo> getActiveMerchant();

    List<MerchantInfo> queryBy(@Param("name") String name,
                               @Param("status") String status,
                               @Param("payKey") String payKey,
                               @Param("paySecret") String paySecret);

    int getActiveMerchantCount();
}