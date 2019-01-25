package com.nucc.service.alipay.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.BankCardInfoMapper;
import com.nucc.entity.alipay.BankCardInfo;
import com.nucc.service.alipay.BankCardInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("bankCardInfoService")
@Transactional
public class BankCardInfoServiceImpl extends BaseService<BankCardInfoMapper, BankCardInfo> implements BankCardInfoService {
    private Logger log = LoggerFactory.getLogger("MerchantInfoServiceImpl");

    @Override
    public ResultData add(BankCardInfo address) throws Exception {
        if (address == null) {
            return ResultData.getErrResult("银行卡信息不能为空");
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
    public ResultData queryBankCardByKey(String merchantId) throws Exception {
        if (StringUtils.isEmpty(merchantId)) {
            return ResultData.getErrResult("商户编号为空");
        }

        List<BankCardInfo> contactInfoList = mapper.queryByKey(merchantId);
        return ResultData.getSuccessResult(contactInfoList);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        return null;
    }

    @Override
    public ResultData update(BankCardInfo obj) throws Exception {
        return null;
    }


}
