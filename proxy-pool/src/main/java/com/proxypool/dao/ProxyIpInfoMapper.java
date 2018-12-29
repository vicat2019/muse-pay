package com.proxypool.dao;


import com.proxypool.entry.ProxyIpInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProxyIpInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProxyIpInfo record);

    ProxyIpInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ProxyIpInfo record);

    List<String> getAllProxyCode();

    List<ProxyIpInfo> queryProxy();

    int delProxyIpSoft(List<Integer> list);

    List<ProxyIpInfo> getFrontProxy();

    List<ProxyIpInfo> getRepeatProxy();

    int delProxyByIds(List<Integer> list);

    int getCountByIp(String ip);

    List<ProxyIpInfo> getUsableProxyIp(@Param("size") int size);

    int updateStatusInfo(ProxyIpInfo proxyIpInfo);
}