package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.entry.DoctorInfo;


/**
 * Created by Vincent on 2019/02/23.
 */
public interface DoctorInfoService {

    ResultData add(DoctorInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(DoctorInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;
}
