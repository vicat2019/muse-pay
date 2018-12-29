package com.nucc.service.alipay.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.AddressInfoMapper;
import com.nucc.entity.alipay.AddressInfo;
import com.nucc.service.alipay.AddressInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("addressInfoService")
@Transactional
public class AddressInfoServiceImpl extends BaseService<AddressInfoMapper, AddressInfo> implements AddressInfoService {
    private Logger log = LoggerFactory.getLogger("MerchantInfoServiceImpl");

    @Override
    public ResultData add(AddressInfo address) throws Exception {
        if (address == null) {
            return ResultData.getErrResult("参数地址对象不能为空");
        }

        // 检查是否缺少参数
        ResultData validateResult = address.legalParam();
        if (!validateResult.isOk()) {
            return validateResult;
        }

        mapper.insert(address);

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData queryAddressByKey(String merchantId) throws Exception {
        if (StringUtils.isEmpty(merchantId)) {
            return ResultData.getErrResult("商户编号为空");
        }

        List<AddressInfo> contactInfoList = mapper.queryByKey(merchantId);
        return ResultData.getSuccessResult(contactInfoList);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        return null;
    }

    @Override
    public ResultData update(AddressInfo obj) throws Exception {
        return null;
    }


}
