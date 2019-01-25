package com.muse.pay.service;

import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderInfo;

public interface PayAdminService {

    /**
     * 查询订单列表
     *
     * @param name      用户名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页记录数
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData<PageInfo<OrderInfo>> queryOrder(String name, String startTime, String endTime, int status,
                                               int pageNum, int pageSize) throws Exception;


}
