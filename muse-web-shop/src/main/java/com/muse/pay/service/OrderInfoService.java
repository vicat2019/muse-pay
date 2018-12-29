package com.muse.pay.service;

import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.entity.vo.OrderInfoVO;

import java.util.List;


/**
 * Created by code generator  on 2018/07/26.
 */
public interface OrderInfoService {

    ResultData add(OrderInfoVO obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(OrderInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData getUserOrders(int userId, String startTime, String endTime, int status, int pageNum, int pageSize) throws Exception;

    ResultData getUserOrderCount(int userId) throws Exception;

    ResultData<OrderInfo> getOrderByNo(String orderNo) throws Exception;

    ResultData setOrderStatus(String orderNo, int status) throws Exception;

    ResultData addOrder(OrderInfoVO orderInfoVO) throws Exception;

    ResultData<PageInfo<OrderInfo>> getOrderByCondition(List<Integer> ids, String startTime, String endTime, int status,
                                                        int pageNum, int pageSize) throws Exception;

    ResultData updateOrderByNo(OrderInfo orderInfo) throws Exception;

    ResultData addRechargeOrder(OrderInfoVO orderInfoVO) throws Exception;
}
