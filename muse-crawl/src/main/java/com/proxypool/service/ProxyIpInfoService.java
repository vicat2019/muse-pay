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

    /**
     * 添加代理IP信息
     *
     * @param proxyIpInfo 代理信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData add(ProxyIpInfo proxyIpInfo) throws Exception;

    /**
     * 删除代理地址信息
     *
     * @param id ID标识
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData del(Integer id) throws Exception;

    /**
     * 更新代理地址信息
     *
     * @param proxyIpInfo 代理信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData update(ProxyIpInfo proxyIpInfo) throws Exception;

    /**
     * 根据IP查询代理地址信息
     *
     * @param id ID标识
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData get(Integer id) throws Exception;

    /**
     * 查询所有代理IP信息编号
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData getAllProxyCode() throws Exception;

    /**
     * 检查代理IP地址信息
     *
     * @throws Exception 异常
     */
    void checkProxyIp() throws Exception;

    /**
     * 获取前面的代理IP信息
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData getFrontProxy() throws Exception;

    /**
     * 删除重复的代理地址信息
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData delRepeatProxy() throws Exception;

    /**
     * 根据IP查询记录数
     *
     * @param ip IP地址
     * @return ResultData
     * @throws Exception 异常
     */
    int getCountByIp(String ip) throws Exception;

    /**
     * 查询指定数量可用的代理IP地址
     *
     * @param size 指定数量
     * @return ResultData
     * @throws Exception 异常
     */
    List<ProxyIpInfo> getUsableProxyIp(int size) throws Exception;

    /**
     * 更新代理IP统计信息
     *
     * @param proxyIpInfo 代理信息
     * @throws Exception 异常
     */
    void updateProxyIpStatusInfo(ProxyIpInfo proxyIpInfo) throws Exception;

    /**
     * 设置代理IP信息状态
     *
     * @param id     ID标识
     * @param status 状态
     * @return int
     * @throws Exception 异常
     */
    int setProxyIpStatus(int id, String status) throws Exception;

    /**
     * 检查指定数量的代理IP是否可用
     *
     * @param size 检查的数量
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData checkAvailability(int size) throws Exception;

    /**
     * 根据IP修改代理信息状态
     *
     * @param ip     IP地址
     * @param status 状态
     * @return int
     * @throws Exception 异常
     */
    int setStatusByIp(String ip, String status) throws Exception;
}
