package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.ConsigneeInfo;


/**
 * Created by code generator  on 2018/08/23.
 */
public interface ConsigneeInfoService {

    ResultData add(ConsigneeInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ConsigneeInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData query(ConsigneeInfo consigneeInfo) throws Exception;
}
