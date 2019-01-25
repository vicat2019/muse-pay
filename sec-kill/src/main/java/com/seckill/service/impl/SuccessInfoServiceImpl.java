package com.seckill.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.seckill.dao.SuccessInfoMapper;
import com.seckill.entity.SuccessInfo;
import com.seckill.service.SuccessInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Created by Vincent on 2018/12/24.
 */
@Service("successInfoService")
@Transactional
public class SuccessInfoServiceImpl extends BaseService<SuccessInfoMapper, SuccessInfo> implements SuccessInfoService {

    @Override
    public ResultData add(SuccessInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(SuccessInfo obj) throws Exception {
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

        SuccessInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
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
