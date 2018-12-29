package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.pay.dao.ConsigneeInfoMapper;
import com.muse.pay.entity.ConsigneeInfo;
import com.muse.pay.service.ConsigneeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by code generator  on 2018/08/23.
 */
@Service("consigneeInfoService")
@Transactional
public class ConsigneeInfoServiceImpl extends BaseService<ConsigneeInfoMapper, ConsigneeInfo> implements ConsigneeInfoService {
    private Logger log = LoggerFactory.getLogger("ConsigneeInfoServiceImpl");

    @Override
    public ResultData add(ConsigneeInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData update(ConsigneeInfo obj) throws Exception {
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

        ConsigneeInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData query(ConsigneeInfo consigneeInfo) throws Exception {
        if (consigneeInfo == null) {
            return ResultData.getErrResult("参数异常");
        }

        List<ConsigneeInfo> consigneeList = mapper.selectByColumn(consigneeInfo);
        if (consigneeList == null) {
            consigneeList = new ArrayList<>();
        }
        log.info("查询常用联系人列表，结果数=" + consigneeList.size());

        return ResultData.getSuccessResult(consigneeList);
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
