package com.merchant.service;

import com.merchant.entity.FilePathTypeEnum;
import com.merchant.entity.MerchantBaseInfo;
import com.merchant.entity.ResultData;

import java.util.List;


/**
 * Created by Vincent on 2019/03/06.
 */
public interface MerchantBaseService {

    ResultData add(MerchantBaseInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(MerchantBaseInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    List<MerchantBaseInfo> getUnusedMerchantBase();

    int insertByBatch(List<MerchantBaseInfo> merchantBaseList);

    int updateStatus(String status, int id);

    int softDeleteById(int id);


    /**
     * 格式化商户名称
     *
     * @param name         商户名称
     * @param originalName 原始名称
     * @return String
     */
    String formatMerchantName(String name, String originalName) throws Exception;

    /**
     * 获取商户名称列表
     *
     * @param filePath      Excel路径
     * @param pathType      路径类型
     * @param doorPicFolder 门头照文件夹地址
     * @return List
     */
    List<MerchantBaseInfo> getMerchantBase(String filePath, FilePathTypeEnum pathType, String doorPicFolder) throws Exception;

    /**
     * 初始化新增数据
     *
     * @param filePath      数据文件
     * @param pathType      文件类型
     * @param doorPicFolder 门头照目录
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData initMerchantBase(String filePath, FilePathTypeEnum pathType, String doorPicFolder) throws Exception;

}
