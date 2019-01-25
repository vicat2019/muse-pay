package com.nucc.dao.weixin;


import com.nucc.entity.weixin.WXRefundInfo;

import java.util.List;

public interface WXRefundInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WXRefundInfo record);

    WXRefundInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(WXRefundInfo record);

    List<WXRefundInfo> getRefundByOutTradeNo(String outTradeNo);

    WXRefundInfo getRefundByOutRefundNo(String outRefundNo);

}