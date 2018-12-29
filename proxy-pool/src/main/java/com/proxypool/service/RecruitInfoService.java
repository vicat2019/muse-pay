package com.proxypool.service;


import com.muse.common.entity.ResultData;
import com.proxypool.entry.RecruitInfo;

/**
 * Created by code generator  on 2018/11/04.
 */
public interface RecruitInfoService {

    ResultData add(RecruitInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(RecruitInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData delRepeatRecruit() throws Exception;
}
