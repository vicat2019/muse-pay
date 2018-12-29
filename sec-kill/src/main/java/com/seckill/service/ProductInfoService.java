package com.seckill.service;


import com.muse.common.entity.ResultData;
import com.seckill.entity.ProductInfo;

/**
 * Created by Vincent on 2018/12/24.
 */
public interface ProductInfoService {

    ResultData add(ProductInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ProductInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData startSecKill(String code, int userId) throws Exception;

    ResultData startByExecutor(String code) throws Exception;
}
