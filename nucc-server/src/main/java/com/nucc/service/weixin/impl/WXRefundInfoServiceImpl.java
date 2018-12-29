package com.nucc.service.weixin.impl;

import com.nucc.dao.weixin.WXRefundInfoMapper;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXRefundInfo;
import com.nucc.entity.weixin.WXTradeInfo;
import com.nucc.entity.weixin.requestentity.WXRefundInfoVO;
import com.nucc.entity.weixin.responseentity.WXTradeResponse;
import com.nucc.service.alipay.BaseService;
import com.nucc.service.weixin.WXRefundInfoService;
import com.nucc.service.weixin.WXTradeInfoService;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by code generator  on 2018/12/02.
 */
@Service("wxRefundInfoService")
@Transactional
public class WXRefundInfoServiceImpl extends BaseService<WXRefundInfoMapper, WXRefundInfo> implements WXRefundInfoService {
    private Logger log = LoggerFactory.getLogger("WXRefundInfoServiceImpl");

    @Autowired
    private WXTradeInfoService wxTradeInfoService;

    @Override
    public WXBaseResponse add(WXRefundInfoVO obj) throws Exception {
        if (obj == null || StringUtils.isEmpty(obj.getOut_refund_no())) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }

        // 检查交易记录是否为空
        WXTradeInfo tradeInfo = wxTradeInfoService.getTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("交易记录[" + obj.getOut_trade_no() + "]不存在");
        }

        // 检查交易记录是否已经有退款
        WXRefundInfo refundInfo = mapper.getRefundByOutRefundNo(obj.getOut_refund_no());
        if (refundInfo != null) {
            return WXBaseResponse.getErrResult("退款单号[" + obj.getOut_refund_no() + "]已存在");
        }

        // 保存
        refundInfo = obj.getRefundInfo();
        refundInfo.setRefund_id(TextUtils.getRandomNum(28));
        int result = mapper.insert(refundInfo);
        log.info("保存申请退款，结果=" + result);

        // 返回结果
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setRefundResponse(tradeInfo, refundInfo);
        return response;
    }


    @Override
    public WXBaseResponse querySingleRefund(WXRefundInfoVO obj) throws Exception {
        if (obj == null) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }

        // 检查商户退款单号和订单号是否为空
        if (StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("商户订单号不能为空");
        }
        if (StringUtils.isEmpty(obj.getOut_refund_no())) {
            return WXBaseResponse.getErrResult("退款单号不能为空");
        }

        // 查询交易记录
        WXTradeInfo tradeInfo = wxTradeInfoService.getTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("交易记录[" + obj.getOut_trade_no() + "]不存在");
        }

        // 根据退款单号查询退款记录
        WXRefundInfo refundInfo = mapper.getRefundByOutRefundNo(obj.getOut_refund_no());
        if (refundInfo == null) {
            return WXBaseResponse.getErrResult("退款记录[" + obj.getOut_refund_no() + "]不存在");
        }

        // 返回结果
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setRefundQueryResponse(tradeInfo, refundInfo);
        return response;
    }

    @Override
    public WXBaseResponse queryRefund(WXRefundInfoVO obj) throws Exception {
        if (obj == null) {
            return WXBaseResponse.getErrResult("参数不能为空");
        }

        // 检查订单号
        if (StringUtils.isEmpty(obj.getOut_trade_no())) {
            return WXBaseResponse.getErrResult("商户订单号不能为空");
        }

        // 查询订单是否存在
        WXTradeInfo tradeInfo = wxTradeInfoService.getTradeByOutTradeNo(obj.getOut_trade_no());
        if (tradeInfo == null) {
            return WXBaseResponse.getErrResult("订单[" + obj.getOut_trade_no() + "]不存在");
        }

        // 查询退款记录
        List<WXRefundInfo> refundList = mapper.getRefundByOutTradeNo(obj.getOut_trade_no());
        if (refundList == null || refundList.size() == 0) {
            return WXBaseResponse.getErrResult("该订单的退款申请为空");
        }

        // 返回内容
        WXTradeResponse response = WXTradeResponse.getSuccessResult();
        response.setTradeQueryAllResponse(tradeInfo, refundList);
        return response;
    }

    @Override
    public WXBaseResponse get(Integer id) throws Exception {
        if (id == 0) {
            return WXBaseResponse.getErrResult("ID不能为空");
        }

        return null;
    }

    @Override
    public WXBaseResponse del(Integer id) throws Exception {
        if (id == 0) {
            return WXBaseResponse.getErrResult("ID不能为空");
        }

        return null;
    }


}
