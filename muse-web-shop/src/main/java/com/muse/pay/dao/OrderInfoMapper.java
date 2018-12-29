package com.muse.pay.dao;

import com.muse.pay.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderInfoMapper {

    Integer deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    int updateByOrderNo(OrderInfo orderInfo);

    List<OrderInfo> getOrderByCondition(Map<String, Object> paramMap);

    Integer getCountByUserId(@Param("userId") int userId);

    OrderInfo selectByNo(@Param("orderNo") String orderNo);

    void updateOrderStatus(@Param("status") int status, @Param("orderNo") String orderNo);
}