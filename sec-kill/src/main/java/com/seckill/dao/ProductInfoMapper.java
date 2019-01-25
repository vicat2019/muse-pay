package com.seckill.dao;

import com.seckill.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

public interface ProductInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ProductInfo record);

    int getProductStock(@Param("code") String code);

    int inventoryDeduction(@Param("code") String code);

    ProductInfo getProductByCode(@Param("code") String code);
}