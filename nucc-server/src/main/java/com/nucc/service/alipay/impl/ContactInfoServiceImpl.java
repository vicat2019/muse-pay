package com.nucc.service.alipay.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.ContactInfoMapper;
import com.nucc.entity.alipay.ContactInfo;
import com.nucc.service.alipay.ContactInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("contactInfoService")
@Transactional
public class ContactInfoServiceImpl extends BaseService<ContactInfoMapper, ContactInfo> implements ContactInfoService {
    private Logger log = LoggerFactory.getLogger("MerchantInfoServiceImpl");

    @Override
    public ResultData add(ContactInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 检查是否缺少参数
        ResultData validateResult = obj.legalParam();
        if (!validateResult.isOk()) {
            return validateResult;
        }

        mapper.insert(obj);

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData queryContactByKey(String merchantId) throws Exception {
        if (StringUtils.isEmpty(merchantId)) {
            return ResultData.getErrResult("商户编号为空");
        }

        List<ContactInfo> contactInfoList = mapper.selectByMerchantId(merchantId);
        return ResultData.getSuccessResult(contactInfoList);
    }

    @Override
    public ResultData delByMerchantId(String merchantId) throws Exception {
        if (StringUtils.isEmpty(merchantId)) {
            log.error("删除联系信息，merchantId不能为空");
            return ResultData.getErrResult("参数不能为空");
        }

        int count = mapper.deleteByMerchantId(merchantId);
        return ResultData.getSuccessResult(Integer.valueOf(count));
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        return null;
    }

    @Override
    public ResultData update(ContactInfo obj) throws Exception {
        return null;
    }


}
