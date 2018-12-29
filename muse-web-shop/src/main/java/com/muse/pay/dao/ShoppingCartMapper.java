package com.muse.pay.dao;

import com.muse.pay.entity.ShoppingCartInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCartInfo record);

    int insertSelective(ShoppingCartInfo record);

    ShoppingCartInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCartInfo record);

    int updateByPrimaryKey(ShoppingCartInfo record);

    ShoppingCartInfo getShoppingCartBy(@Param("userId") int userId, @Param("bookId") int bookId);

    List<ShoppingCartInfo> selectByUserId(int userId);

    Integer getCountByUserId(int userId);

    void deleteByUserId(int userId);

    int delByUserIdAndBookIds(@Param("userId") int userId, @Param("list") List<Integer> list);
}