package com.proxypool.dao;


import com.proxypool.entry.ProxyIpInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 代理信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface ProxyIpInfoMapper {
    /**
     * 根据主键删除代理信息
     *
     * @param id ID标识
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存代理信息
     *
     * @param record 代理信息
     * @return int
     */
    int insert(ProxyIpInfo record);

    /**
     * 根据ID查询代理信息
     *
     * @param id ID标识
     * @return ProxyIpInfo
     */
    ProxyIpInfo selectByPrimaryKey(Integer id);

    /**
     * 根据主键修改代理信息
     *
     * @param record 代理信息
     * @return int
     */
    int updateByPrimaryKey(ProxyIpInfo record);

    /**
     * 获取所有代理信息编码
     *
     * @return List
     */
    List<String> getAllProxyCode();

    /**
     * 查询代理信息
     *
     * @return List
     */
    List<ProxyIpInfo> queryProxy();

    /**
     * 软删除代理信息
     *
     * @param list 要软删除的代理信息列表
     * @return int
     */
    int delProxyIpSoft(List<Integer> list);

    /**
     * 查询排在前面的代理信息列表
     *
     * @return List
     */
    List<ProxyIpInfo> getFrontProxy();

    /**
     * 查询重复的代理信息
     *
     * @return List
     */
    List<ProxyIpInfo> getRepeatProxy();

    /**
     * 根据ID集合删除代理信息
     *
     * @param list 要删除的代理信息ID集合
     * @return
     */
    int delProxyByIds(List<Integer> list);

    /**
     * 根据IP查询代理信息记录个数
     *
     * @param ip IP地址
     * @return int
     */
    int getCountByIp(String ip);

    /**
     * 查询可用的指定数量的代理信息
     *
     * @param size 查询个数
     * @return List
     */
    List<ProxyIpInfo> getUsableProxyIp(@Param("size") int size);

    /**
     * 更新代理信息
     *
     * @param proxyIpInfo 代理信息
     * @return int
     */
    int updateStatusInfo(ProxyIpInfo proxyIpInfo);

    /**
     * 设置代理信息状态
     *
     * @param id     代理信息ID
     * @param status 状态值
     * @return int
     */
    int setProxyIpStatus(@Param("id") int id, @Param("status") String status);

    /**
     * 查询指定个数的最近的代理信息
     *
     * @param size 指定的个数
     * @return List
     */
    List<ProxyIpInfo> getRecentlyIp(@Param("size") int size);

    /**
     * 根据IP设置代理记录状态
     *
     * @param ip     IP地址
     * @param status 状态值
     * @return int
     */
    int setStatusByIp(@Param("ip") String ip, @Param("status") String status);
}