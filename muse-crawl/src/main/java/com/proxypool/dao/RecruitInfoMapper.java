package com.proxypool.dao;

import com.proxypool.entry.RecruitInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: JL信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface RecruitInfoMapper {

    /**
     * 根据主键删除记录
     *
     * @param id 主键ID
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增JL信息
     *
     * @param record JL信息
     * @return int
     */
    int insert(RecruitInfo record);

    /**
     * 根据主键查询JL信息
     *
     * @param id 主键ID
     * @return RecruitInfo
     */
    RecruitInfo selectByPrimaryKey(Integer id);

    /**
     * 根据主键更新JL信息
     *
     * @param record JL信息
     * @return int
     */
    int updateByPrimaryKey(RecruitInfo record);

    /**
     * 根据编码获取JL个数
     *
     * @param code 编码
     * @return int
     */
    int getCountByCode(String code);

    /**
     * 查询重复的JL信息集合
     *
     * @return List
     */
    List<Integer> getRepeatRecruitIds();

    /**
     * 根据ID集合删除JL信息
     *
     * @param idList 要删除的JL记录ID集合
     * @return int
     */
    int delRepeatByIds(List<Integer> idList);

    /**
     * 根据编码查询JL信息
     *
     * @param code 编码
     * @return RecruitInfo
     */
    RecruitInfo getByIdentificationCode(String code);

    /**
     * 根据编码更新JL信息
     *
     * @param recruitInfo JL信息
     * @return int
     */
    int updateByCode(RecruitInfo recruitInfo);

    /**
     * 查询JL列表
     *
     * @return List
     */
    List<RecruitInfo> queryRecruit(@Param("companyName") String companyName, @Param("postName") String postName,
                                   @Param("minSalary") String minSalary, @Param("maxSalary") String maxSalary,
                                   @Param("releaseTime") String releaseTime, @Param("createTime") String createTime);


    /**
     * 查询JL列表(用于清洗数据)
     *
     * @return List
     */
    List<RecruitInfo> selectRecruit();

    /**
     * 更新薪资属性值
     *
     * @param recruitInfos JL信息
     * @return int
     */
    int updateBatch(List<RecruitInfo> recruitInfos);

}