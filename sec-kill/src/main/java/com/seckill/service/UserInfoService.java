package com.seckill.service;


import com.muse.common.entity.ResultData;
import com.seckill.entity.UserInfo;

/**
 * Created by Vincent on 2018/12/24.
 */
public interface UserInfoService {

    ResultData add(UserInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(UserInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData genRandomUser(int size) throws Exception;
}
