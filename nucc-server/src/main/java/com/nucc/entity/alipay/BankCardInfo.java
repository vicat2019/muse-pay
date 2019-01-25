package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: 银行卡信息类
 * @author: Vincent
 * @create: 2018-11-27 09:15
 **/
public class BankCardInfo extends BaseEntityInfo {

    private String sub_merchant_id;

    // 银行卡号
    private String card_no;

    // 银行持卡人姓名
    private String card_name;

    /**
     * 检查地址参数是否合法
     *
     * @return
     */
    public ResultData legalParam() {
        if (StringUtils.isEmpty(sub_merchant_id)) {
            return ResultData.getErrResult("商戶编号不能为空");
        }
        if (StringUtils.isEmpty(card_no)) {
            return ResultData.getErrResult("银行卡号不能为空");
        }
        if (StringUtils.isEmpty(card_name)) {
            return ResultData.getErrResult("持卡人名称不能为空");
        }

        return ResultData.getSuccessResult();
    }


    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }
}
