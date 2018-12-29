package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.CodeInfo;
import org.springframework.stereotype.Service;


/**
 * Created by code generator  on 2018/07/24.
 */
@Service("codeService")
public interface CodeInfoService {

    ResultData add(CodeInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(CodeInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData verifyCode(String email, String code, String codeType) throws Exception;

    ResultData makeVerifyCode(String email, String codeType) throws Exception;

    
}
