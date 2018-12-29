package com.nucc.dao.weixin;


import com.nucc.entity.weixin.WXTradeInfo;
import org.apache.ibatis.annotations.Param;

public interface WXTradeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WXTradeInfo record);

    WXTradeInfo selectByPrimaryKey(Integer id);

    int updateByTransactionId(WXTradeInfo record);

    WXTradeInfo getWXTradeByOutTradeNo(String outTradeNo);

    int setTradePayStatus(@Param("outTradeNo") String outTradeNo,
                          @Param("tradeStatus") String status);

    Integer getCountByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    WXTradeInfo getTradeByTransactionId(String transactionId);
}