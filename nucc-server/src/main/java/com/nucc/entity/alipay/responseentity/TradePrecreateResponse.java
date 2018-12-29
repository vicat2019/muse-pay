package com.nucc.entity.alipay.responseentity;

import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: 预下单返回内容
 * @author: Vincent
 * @create: 2018-11-26 18:13
 **/
public class TradePrecreateResponse extends BaseResponseEntity {

    private String out_trade_no;

    private String qr_code;

    /**
     * 默认构造方法
     */
    public TradePrecreateResponse() {
        this.setOkValue();
    }

    /**
     * 构造方法
     *
     * @param out_trade_no 商户订单号
     * @param qr_code      二维码地址
     */
    public TradePrecreateResponse(String out_trade_no, String qr_code) {
        this.out_trade_no = out_trade_no;
        this.qr_code = qr_code;
    }

    /**
     * 返回默认值
     *
     * @param tradeParams
     * @return
     * @throws Exception
     */
    public static TradePrecreateResponse getDefaultBy(TradeInfoVO tradeParams) throws Exception {
        if (tradeParams == null || StringUtils.isEmpty(tradeParams.getOut_trade_no())) {
            throw new Exception("商户订单号为空");
        }

        String qrCodeUrl = "http://www.baidu.com";
        return new TradePrecreateResponse(tradeParams.getOut_trade_no(), qrCodeUrl);
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
