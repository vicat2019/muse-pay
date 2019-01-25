package com.muse.pay.service.impl;

import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.service.OrderInfoService;
import com.muse.pay.service.PayAdminService;
import com.muse.pay.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service("payAdminService")
public class PayAdminServiceImpl implements PayAdminService {
    private Logger log = LoggerFactory.getLogger("PayAdminServiceImpl");

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private UserService userService;


    @Override
    public ResultData<PageInfo<OrderInfo>> queryOrder(String name, String startTime, String endTime, int status,
                                                      int pageNum, int pageSize) throws Exception {

        long start = System.currentTimeMillis();

        List<Integer> userIdList = null;
        if (!StringUtils.isEmpty(name)) {
            ResultData idResultData = userService.getUserIdByName(name);
            if (idResultData.isOk() && !idResultData.resultIsEmpty()) {
                userIdList = (List<Integer>) idResultData.getData();
            }

            if (userIdList == null || userIdList.size()==0) {
                return ResultData.getErrResult("用户不存在");
            }
        }

        // 根据页码分页查询订单
        ResultData<PageInfo<OrderInfo>> resultData = orderInfoService.getOrderByCondition(userIdList, startTime, endTime,
                status, pageNum, pageSize);

        log.info("查询订单列表，QUERY_ORDER耗时=" + ((System.currentTimeMillis() - start) / 1000d));
        return resultData;
    }
}
