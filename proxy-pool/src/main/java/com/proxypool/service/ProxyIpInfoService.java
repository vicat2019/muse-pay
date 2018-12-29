package com.proxypool.service;


import com.muse.common.entity.ResultData;
import com.proxypool.entry.ProxyIpInfo;

import java.util.List;

/**
 * @Description: 代理信息服务接口
 * @Author: Vincent
 * @Date: 2018/10/15
 */
public interface ProxyIpInfoService {

    ResultData add(ProxyIpInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(ProxyIpInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData getAllProxyCode() throws Exception;

    void checkProxyIp() throws Exception;

    ResultData getFrontProxy() throws Exception;

    ResultData delRepeatProxy() throws Exception;

    int getCountByIp(String ip) throws Exception;

    List<ProxyIpInfo> getUsableProxyIp(int size) throws Exception;

    void updateProxyIpStatusInfo(ProxyIpInfo proxyIpInfo) throws Exception;
}
