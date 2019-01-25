package com.proxypool.dao;


import com.proxypool.picture.PictureInfo;

import java.util.List;

/**
 * @Description: 图片信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface PictureInfoMapper {

    /**
     * 新增或更新图片信息
     *
     * @param picture 图片信息
     * @return int
     */
    int insertOrUpdate(PictureInfo picture);

    /**
     * 根据主键更新图片信息
     *
     * @param picture 图片信息
     * @return List
     */
    int updateByPrimary(PictureInfo picture);

    /**
     * 查询图片信息
     *
     * @return List
     */
    List<PictureInfo> queryPicture();

}