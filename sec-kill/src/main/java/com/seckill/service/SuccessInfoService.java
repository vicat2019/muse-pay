package com.seckill.service;


import com.muse.common.entity.ResultData;
import com.seckill.entity.SuccessInfo;

/**
 * Created by Vincent on 2018/12/24.
 */
public interface SuccessInfoService {

    ResultData add(SuccessInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(SuccessInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;
}
