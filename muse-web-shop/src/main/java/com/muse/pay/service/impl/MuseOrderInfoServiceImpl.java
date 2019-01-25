package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.RedisUtil;
import com.muse.pay.dao.MuseOrderInfoMapper;
import com.muse.pay.entity.MuseOrderInfo;
import com.muse.pay.service.MuseOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


/**
 * 支付服务实现类
 * Created by code generator  on 2018/08/08.
 */
@Service("payOrderInfoService")
@Transactional
public class MuseOrderInfoServiceImpl extends BaseService<MuseOrderInfoMapper, MuseOrderInfo> implements MuseOrderInfoService {
    private Logger log = LoggerFactory.getLogger("MuseOrderInfoServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultData add(MuseOrderInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(MuseOrderInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if(obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        MuseOrderInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getByOrderNo(String orderNo) throws Exception {
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("根据订单编号查询订单，参数为空");
        }
        MuseOrderInfo museOrderInfo = mapper.selectByOrderNo(orderNo);
        if (museOrderInfo == null) {
            return ResultData.getErrResult("要查询的订单不存在");
        }

        return ResultData.getSuccessResult(museOrderInfo);
    }

    @Override
    public boolean isExists(String merchantNo, String orderNo) throws Exception {
        if (StringUtils.isEmpty(merchantNo) || StringUtils.isEmpty(orderNo)) {
            return false;
        }

        MuseOrderInfo payOrderInfo = mapper.selectByMerchantAndTradeNo(merchantNo, orderNo);
        if (payOrderInfo == null) {
            return false;
        }

        return true;
    }

    @Override
    public ResultData getByMerchantAndOrderCode(String merchantNo, String orderNo) throws Exception {
        if (StringUtils.isEmpty(merchantNo) || StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("参数不能为空");
        }

        MuseOrderInfo payOrderInfo = mapper.selectByMerchantAndTradeNo(merchantNo, orderNo);
        if (payOrderInfo == null) {
            return ResultData.getErrResult("该订单信息不存在");
        }

        return ResultData.getSuccessResult(payOrderInfo);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }
}
