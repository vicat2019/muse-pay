package com.merchant.service;


import com.merchant.entity.ChannelInfo;
import com.merchant.entity.ResultData;

import java.util.List;

/**
 * Created by Vincent on 2019/03/06.
 */
public interface ChannelInfoService {

    ResultData add(ChannelInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ChannelInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    List<ChannelInfo> getActiveChannel();

    ChannelInfo getChannelByMchId(String mchId) throws Exception;
}
