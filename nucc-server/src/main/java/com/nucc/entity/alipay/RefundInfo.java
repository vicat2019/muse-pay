package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: muse-pay
 * @description: 退款返回结果
 * @author: Vincent
 * @create: 2018-11-26 18:38
 **/
public class RefundInfo extends BaseEntityInfo {

    private String trade_no;

    private String out_trade_no;

    private String out_request_no;

    private BigDecimal refund_amount;

    private BigDecimal total_amount;

    private String refund_reason;

    private Date gmt_refund_pay;

    /**
     * 获取退款信息
     * @param tradeInfoVO
     * @return
     */
    public static RefundInfo getByParams(TradeInfoVO tradeInfoVO) {
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setTrade_no(tradeInfoVO.getTrade_no());
        refundInfo.setOut_trade_no(tradeInfoVO.getOut_trade_no());
        refundInfo.setOut_request_no(tradeInfoVO.getOut_request_no());
        refundInfo.setRefund_amount(tradeInfoVO.getRefund_amount());
        refundInfo.setTotal_amount(tradeInfoVO.getTotal_amount());
        refundInfo.setRefund_reason(tradeInfoVO.getRefund_reason());

        return refundInfo;
    }

    public ResultData legalParam() {
        if (StringUtils.isEmpty(out_trade_no)) {
            return ResultData.getErrResult("商户订单号不能为空");
        }
        if (refund_amount == null) {
            return ResultData.getErrResult("退款金额不能为空");
        }
        if (StringUtils.isEmpty(out_request_no)) {
            return ResultData.getErrResult("退款编号不能为空");
        }

        return ResultData.getSuccessResult();
    }


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public BigDecimal getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(BigDecimal refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public Date getGmt_refund_pay() {
        return gmt_refund_pay;
    }

    public void setGmt_refund_pay(Date gmt_refund_pay) {
        this.gmt_refund_pay = gmt_refund_pay;
    }
}
