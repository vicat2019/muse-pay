package com.proxypool.dao;

import com.proxypool.entry.SubscribeInfo;

public interface SubscribeInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SubscribeInfo record);

    SubscribeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SubscribeInfo record);
}