package com.nucc.dao.alipay;

import com.nucc.entity.alipay.TradeInfo;
import org.apache.ibatis.annotations.Param;

public interface TradeInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TradeInfo tradeInfo);

    TradeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(TradeInfo tradeInfo);

    TradeInfo getTradeByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    int closeTrade(@Param("outTradeNo") String outTradeNo);

    TradeInfo getTradeByTradeNo(@Param("tradeNo") String tradeNo);

    int updateTradeSucc(@Param("tradeNo") String tradeNo);
}