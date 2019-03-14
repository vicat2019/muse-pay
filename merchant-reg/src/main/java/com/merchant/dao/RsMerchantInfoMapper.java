package com.merchant.dao;

import com.merchant.entity.RuiShengUserInfo;

import java.util.List;
import java.util.Map;

public interface RsMerchantInfoMapper {

    /**
     * 根据ID删除数据
     *
     * @param id ID
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加数据
     *
     * @param record 数据
     * @return int
     */
    int insert(RuiShengUserInfo record);

    /**
     * 根据ID查询数据
     *
     * @param id ID
     * @return RuiShengUserInfo
     */
    RuiShengUserInfo selectByPrimaryKey(Integer id);

    /**
     * 根据ID更新数据
     *
     * @param record 数据
     * @return int
     */
    int updateByPrimaryKey(RuiShengUserInfo record);

    /**
     * 查询数据
     *
     * @param record 查询条件
     * @return List
     */
    List<RuiShengUserInfo> queryRuiShengUser(RuiShengUserInfo record);

    /**
     * 查询所有名称
     *
     * @return List
     */
    List<String> getAllName();

    List<RuiShengUserInfo> query(Map<String, Object> params);

    int insertBatch(List<RuiShengUserInfo> list);

    int updateBatch(Map<String, Object> params);
}