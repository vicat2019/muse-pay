package com.nucc.entity.alipay.responseentity;

import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.TradeInfo;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import com.nucc.util.TextUtils;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: 统一收单交易撤销返回内容
 * @author: Vincent
 * @create: 2018-11-26 18:13
 **/
public class TradeCancelResponse extends BaseResponseEntity {

    private String out_trade_no;

    private String trade_no;

    /**
     * 根据参数生成撤销订单返回对象
     *
     * @param tradePayParams
     * @return
     * @throws Exception
     */
    public static TradeCancelResponse getDefaultBy(TradeInfoVO tradePayParams) throws Exception {
        if (tradePayParams == null || StringUtils.isEmpty(tradePayParams.getOut_trade_no())) {
            throw new Exception("商户订单号不能为空");
        }

        TradeCancelResponse response = new TradeCancelResponse();
        response.setOkValue();
        response.setOut_trade_no(tradePayParams.getOut_trade_no());
        response.setTrade_no(TextUtils.makeTradeNo());

        return response;
    }

    public static TradeCancelResponse getDefaultBy(TradeInfo tradeInfo) throws Exception {
        TradeCancelResponse response = new TradeCancelResponse();
        response.setOkValue();
        response.setOut_trade_no(tradeInfo.getOut_trade_no());
        response.setTrade_no(tradeInfo.getTrade_no());

        return response;
    }



    // 是否需要重试
    private String retry_flag;

    // 本次撤销触发的交易动作close:关闭交易,无退款; refund:产生了退款
    private String action;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getRetry_flag() {
        return retry_flag;
    }

    public void setRetry_flag(String retry_flag) {
        this.retry_flag = retry_flag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
