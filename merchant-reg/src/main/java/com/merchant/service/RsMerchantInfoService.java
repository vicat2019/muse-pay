package com.merchant.service;

import com.merchant.entity.ChannelInfo;
import com.merchant.entity.ResultData;
import com.merchant.entity.RuiShengUserInfo;

import java.util.List;
import java.util.Map;


/**
 * @Description: 商户数据服务接口
 * @Author: Vincent
 * @Date: 2019/2/16
 */
public interface RsMerchantInfoService {

    /**
     * 添加商户信息
     *
     * @param obj 商户信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData add(RuiShengUserInfo obj) throws Exception;

    /**
     * 删除商户
     *
     * @param id ID
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData del(Integer id) throws Exception;

    /**
     * 更新商户
     *
     * @param obj 商户信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData update(RuiShengUserInfo obj) throws Exception;


    int updateBatch(Map<String, Object> params) throws Exception;

    /**
     * 根据ID查询商户
     *
     * @param id ID值
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData get(Integer id) throws Exception;

    /**
     * 查询商户
     *
     * @param shengUserInfo 查询条件
     * @param pageNum       页码
     * @param size          每页记录数
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData queryRuiShengUser(RuiShengUserInfo shengUserInfo, int pageNum, int size) throws Exception;

    /**
     * 查询所有商户名称
     *
     * @return List
     * @throws Exception 异常
     */
    List<String> getAllName() throws Exception;


    List<RuiShengUserInfo> query(Map<String, Object> params);


    /**
     * 商户进件
     *
     * @param userInfo    商户信息
     * @param channelInfo 通道信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData insert(RuiShengUserInfo userInfo, ChannelInfo channelInfo) throws Exception;


    /**
     * 商户进件
     *
     * @param list 商户信息列表
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData insertBatch(List<RuiShengUserInfo> list) throws Exception;

    /**
     * 修改费率
     *
     * @param rate                    费率
     * @param mchId                   代理商户号
     * @param submchId                商户编号
     * @param channelRatePropertyName 渠道费率属性名称
     * @param privateKey              秘钥
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData modifyRate(double rate, String mchId, String submchId, String channelRatePropertyName, String privateKey) throws Exception;

}
