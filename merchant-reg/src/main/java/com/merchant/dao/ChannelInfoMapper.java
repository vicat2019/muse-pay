package com.merchant.dao;

import com.merchant.entity.ChannelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ChannelInfo record);

    ChannelInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ChannelInfo record);

    List<ChannelInfo> getActiveChannel();

    ChannelInfo getChannelByMchId(@Param("mchId") String mchId);
}