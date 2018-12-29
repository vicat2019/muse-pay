package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.pay.dao.OrderItemInfoMapper;
import com.muse.pay.entity.OrderItemInfo;
import com.muse.pay.service.OrderItemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by code generator  on 2018/07/26.
 */
@Service("orderItemInfoService")
public class OrderItemInfoServiceImpl extends BaseService<OrderItemInfoMapper, OrderItemInfo> implements OrderItemInfoService {
    private Logger log = LoggerFactory.getLogger("OrderItemInfoServiceImpl");

    @Override
    public ResultData add(OrderItemInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(OrderItemInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        OrderItemInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData insertByBatch(List<OrderItemInfo> orderItemList) throws Exception {
        // 检查参数
        if (orderItemList == null || orderItemList.size() == 0) {
            return ResultData.getErrResult("参数不能为空");
        }

        int count = mapper.insertByBatch(orderItemList);
        log.info("批量添加订单内容项=" + count);

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }
}
