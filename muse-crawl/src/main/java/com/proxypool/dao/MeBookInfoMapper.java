package com.proxypool.dao;


import com.proxypool.kindlebook.MeBookInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 电子书信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface MeBookInfoMapper {

    /**
     * 新增或更新电子书信息
     *
     * @param bookInfo 电子书信息
     * @return int
     */
    int insertOrUpdate(MeBookInfo bookInfo);

    /**
     * 根据主键更新电子书信息
     *
     * @param bookInfo 电子书信息
     * @return int
     */
    int updateByPrimary(MeBookInfo bookInfo);

    /**
     * 查询所有电子书信息
     *
     * @return List
     */
    List<MeBookInfo> getAllMeBook();

    /**
     * 查询电子书信息(按照名称、标题排序)
     *
     * @return List
     */
    List<MeBookInfo> handleMeBook();

    /**
     * 查询电子书记录编码
     *
     * @return List
     */
    List<Integer> getAllCode();

    /**
     * 根据电子书编码查询电子书数量
     *
     * @param code 编码
     * @return int
     */
    int getCountByCode(@Param("code") int code);
}