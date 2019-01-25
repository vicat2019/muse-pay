package com.nucc.entity.alipay.responseentity;

import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import com.nucc.util.TextUtils;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: 创建返回内容
 * @author: Vincent
 * @create: 2018-11-26 18:13
 **/
public class TradeCreatResponse extends BaseResponseEntity {

    private String out_trade_no;

    private String trade_no;

    /**
     * 默认构造方法
     */
    public TradeCreatResponse() {
        this.setOkValue();
    }

    /**
     * 构造方法
     *
     * @param out_trade_no
     * @param trade_no
     */
    public TradeCreatResponse(String out_trade_no, String trade_no) {
        this.out_trade_no = out_trade_no;
        this.trade_no = trade_no;
    }

    /**
     * 生成默认返回对象
     *
     * @param tradePayParam
     * @return
     * @throws Exception
     */
    public static TradeCreatResponse getDefaultBy(TradeInfoVO tradePayParam) throws Exception {
        if (tradePayParam == null || StringUtils.isEmpty(tradePayParam.getOut_trade_no())) {
            throw new Exception("商户订单号为空");
        }

        return new TradeCreatResponse(tradePayParam.getOut_trade_no(), TextUtils.getRandomStr(15));
    }

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
}
