package com.nucc.service.alipay.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.RefundInfoMapper;
import com.nucc.entity.alipay.RefundInfo;
import com.nucc.service.alipay.RefundInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("refundInfoService")
@Transactional
public class RefundInfoServiceImpl extends BaseService<RefundInfoMapper, RefundInfo> implements RefundInfoService {
    private Logger log = LoggerFactory.getLogger("RefundInfoServiceImpl");

    @Override
    public ResultData add(RefundInfo refundInfo) throws Exception {
        if (refundInfo == null) {
            log.error("添加退款记录，参数为空");
            return ResultData.getErrResult("退款参数不能为空");
        }

        // 检查是否缺少参数
        ResultData validateResult = refundInfo.legalParam();
        if (!validateResult.isOk()) {
            log.error("添加退款记录，校验退款信息异常=" + validateResult.getMessage());
            return validateResult;
        }

        // 添加退款记录
        mapper.insert(refundInfo);
        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData queryRefundByKey(String outTradeNo, String outRequestNo) throws Exception {
        if (StringUtils.isEmpty(outTradeNo)) {
            return ResultData.getErrResult("商户编号为空");
        }

        RefundInfo refundInfo = mapper.queryByKey(outTradeNo, outRequestNo);
        return ResultData.getSuccessResult(refundInfo);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        return null;
    }

    @Override
    public ResultData update(RefundInfo obj) throws Exception {
        return null;
    }


}
