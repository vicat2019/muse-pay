package com.muse.pay.dao;

import com.muse.pay.entity.MuseMerchantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MuseMerchantInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MuseMerchantInfo record);

    int insertSelective(MuseMerchantInfo record);

    MuseMerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MuseMerchantInfo record);

    int updateByPrimaryKey(MuseMerchantInfo record);

    MuseMerchantInfo selectByUserNo(@Param("userNo") String userNo);

    String getSecretByUserNo(@Param("userNo") String userNo);

    List<MuseMerchantInfo> selectByColumn(MuseMerchantInfo merchantInfo);

    int selectCountByColumn(MuseMerchantInfo merchantInfo);
}