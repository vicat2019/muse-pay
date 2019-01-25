package com.proxypool.dao;


import com.proxypool.entry.RpSequenceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 序列信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface RpSequenceInfoMapper {
    /**
     * 根据序列删除序列记录
     *
     * @param sequence 序列值
     * @return int
     */
    int deleteBySequence(String sequence);

    /**
     * 保存序列记录
     *
     * @param sequence 序列值
     * @return int
     */
    int insert(String sequence);

    /**
     * 根据序列值查询序列信息
     *
     * @param Sequence 序列值
     * @return RpSequenceInfo
     */
    RpSequenceInfo selectBySequence(String Sequence);

    /**
     * 根据序列值修改序列信息状态
     *
     * @param status     状态值
     * @param sequenceNo 序列
     * @return int
     */
    int updateBySequence(@Param("status") String status, @Param("sequenceNo") String sequenceNo);

    /**
     * 查询指定个数的序列集合
     *
     * @param size 个数
     * @return List
     */
    List<Object> selectSequenceBatch(@Param("size") int size);

    /**
     * 批量保存序列号
     *
     * @param sequenceInfoList 序列号集合
     * @return int
     */
    int insertSequenceBatch(@Param("list") List<String> sequenceInfoList);

    /**
     * 测试SQL
     *
     * @param testlist 集合
     * @return int
     */
    int test(@Param("list") List<String> testlist);

    /**
     * 根据名称查询当前的序列号
     *
     * @param name 名称
     * @return 序列值
     */
    String getCurrentSequenceNum(String name);

    /**
     * 更新当前序列值记录
     *
     * @param value 序列值
     * @param name  名称
     * @return int
     */
    int updateCurrentSequenceNum(@Param("value") String value, @Param("name") String name);

}