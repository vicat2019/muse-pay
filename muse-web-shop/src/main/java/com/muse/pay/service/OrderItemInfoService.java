package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderItemInfo;

import java.util.List;


/**
 * Created by code generator  on 2018/07/26.
 */
public interface OrderItemInfoService {

    ResultData add(OrderItemInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(OrderItemInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData insertByBatch(List<OrderItemInfo> orderItemList) throws Exception;

}
