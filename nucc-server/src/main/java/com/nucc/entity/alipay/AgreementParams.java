package com.nucc.entity.alipay;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 签约信息类
 * @author: Vincent
 * @create: 2018-11-26 15:25
 **/
public class AgreementParams implements Serializable {

    private String agreement_no;

    private String auth_confirm_no;

    private String apply_token;

    public String getAgreement_no() {
        return agreement_no;
    }

    public void setAgreement_no(String agreement_no) {
        this.agreement_no = agreement_no;
    }

    public String getAuth_confirm_no() {
        return auth_confirm_no;
    }

    public void setAuth_confirm_no(String auth_confirm_no) {
        this.auth_confirm_no = auth_confirm_no;
    }

    public String getApply_token() {
        return apply_token;
    }

    public void setApply_token(String apply_token) {
        this.apply_token = apply_token;
    }
}
