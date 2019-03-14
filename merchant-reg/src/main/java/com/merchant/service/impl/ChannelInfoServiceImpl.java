package com.merchant.service.impl;

import com.google.common.base.Preconditions;
import com.merchant.dao.ChannelInfoMapper;
import com.merchant.entity.ChannelInfo;
import com.merchant.entity.ResultData;
import com.merchant.service.BaseService;
import com.merchant.service.ChannelInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by Vincent on 2019/03/06.
 */
@Service("channelInfoService")
@Transactional
public class ChannelInfoServiceImpl extends BaseService<ChannelInfoMapper, ChannelInfo> implements ChannelInfoService {

    @Override
    public ResultData add(ChannelInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(ChannelInfo obj) throws Exception {
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

        ChannelInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public List<ChannelInfo> getActiveChannel() {
        return mapper.getActiveChannel();
    }

    @Override
    public ChannelInfo getChannelByMchId(String mchId) throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(mchId), "参数不能为空");
        return mapper.getChannelByMchId(mchId);
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
