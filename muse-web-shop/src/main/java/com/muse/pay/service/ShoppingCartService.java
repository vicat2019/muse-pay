package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.ShoppingCartInfo;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by code generator  on 2018/07/25.
 */
public interface ShoppingCartService {


    ResultData add(ShoppingCartInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ShoppingCartInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData getUserShoppingCart(int userId) throws Exception;

    int getCountByUserId(int userId) throws Exception;

    ResultData getUserDetail(int userId) throws Exception;

    ResultData emptyCart(int userId) throws Exception;

    ResultData removeShoppingCartItem(int userId, List<Integer> bookIds) throws Exception;

    ResultData buy(String userId, String bookId) throws Exception;

    ResultData recharge(String userId, BigDecimal amount) throws Exception;
}
