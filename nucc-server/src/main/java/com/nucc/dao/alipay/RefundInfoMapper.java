package com.nucc.dao.alipay;

import com.nucc.entity.alipay.RefundInfo;
import org.apache.ibatis.annotations.Param;

public interface RefundInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RefundInfo merchant);

    RefundInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RefundInfo merchant);

    RefundInfo queryByKey(@Param("outTradeNo") String outTradeNo,
                                @Param("outRequestNo") String outRequestNo);
}