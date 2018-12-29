package com.nucc.service.weixin.impl;

import com.muse.common.entity.ResultData;
import com.nucc.dao.weixin.WXSuperMerchantInfoMapper;
import com.nucc.entity.weixin.WXSuperMerchantInfo;
import com.nucc.service.alipay.BaseService;
import com.nucc.service.weixin.WXSuperMerchantInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


/**
 * Created by code generator  on 2018/12/04.
 */
@Service
@Transactional
public class WXSuperMerchantInfoServiceImpl extends BaseService<WXSuperMerchantInfoMapper, WXSuperMerchantInfo> implements WXSuperMerchantInfoService {

    @Override
    public ResultData add(WXSuperMerchantInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(WXSuperMerchantInfo obj) throws Exception {
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

        WXSuperMerchantInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getMerchantByAppId(String appId) throws Exception {
        if (StringUtils.isEmpty(appId)) {
            return ResultData.getErrResult("appId不能为空");
        }

        WXSuperMerchantInfo merchant = mapper.getMerchantByAppId(appId);
        if (merchant == null) {
            return ResultData.getErrResult("appId为[" + appId + "]的商户不存在");
        }

        return ResultData.getSuccessResult(merchant);
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
