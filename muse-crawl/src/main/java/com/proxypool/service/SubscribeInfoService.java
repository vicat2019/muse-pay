package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.entry.SubscribeInfo;


/**
 * Created by Vincent on 2019/02/23.
 */
public interface SubscribeInfoService {

    ResultData add(SubscribeInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(SubscribeInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;
}
