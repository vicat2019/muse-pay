package com.muse.pay.dao;


import com.muse.pay.entity.OrderItemInfo;

import java.util.List;

public interface OrderItemInfoMapper {
    int deleteByPrimaryKey(int id);

    int insert(OrderItemInfo record);

    int insertSelective(OrderItemInfo record);

    OrderItemInfo selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(OrderItemInfo record);

    int updateByPrimaryKeyWithBLOBs(OrderItemInfo record);

    int updateByPrimaryKey(OrderItemInfo record);

    int insertByBatch(List<OrderItemInfo> orderItemList);

    OrderItemInfo selectSmallByOrderNo(String orderNo);
}