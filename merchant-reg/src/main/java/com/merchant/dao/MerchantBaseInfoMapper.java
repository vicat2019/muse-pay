package com.merchant.dao;

import com.merchant.entity.MerchantBaseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantBaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantBaseInfo record);

    MerchantBaseInfo selectByPrimaryKey(Integer id);


    int updateByPrimaryKey(MerchantBaseInfo record);

    List<MerchantBaseInfo> getUnusedMerchantBase();

    int insertByBatch(List<MerchantBaseInfo> list);

    int updateStatus(@Param("status") String status, @Param("id") int id);

    int softDeleteById(@Param("id") int id);

}