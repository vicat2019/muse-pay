package com.proxypool.service;


import com.muse.common.entity.ResultData;
import com.proxypool.entry.RecruitInfo;

import java.util.Map;

/**
 * Created by code generator  on 2018/11/04.
 */
public interface RecruitInfoService {

    ResultData add(RecruitInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(RecruitInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData delRepeatRecruit() throws Exception;

    Map<String, Object> queryRecruit(int page, int size, String companyName, String postName, String minSalary,
                                     String maxSalary, String releaseTime, String createTime) throws Exception;

    /**
     * 清洗数据
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData rinseRecruit(int page, int pageSize) throws Exception;
}
