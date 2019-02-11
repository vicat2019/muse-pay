package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.kindlebook.MeBookInfo;

import java.util.List;

/**
 * @Description: 电子书服务接口
 * @Author: Vincent
 * @Date: 2019/1/29
 */
public interface MeBookService {

    /**
     * 添加电子书
     *
     * @param bookInfo 电子书信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData insert(MeBookInfo bookInfo) throws Exception;

    /**
     * 更新电子书信息
     *
     * @param bookInfo 电子书信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData update(MeBookInfo bookInfo) throws Exception;

    /**
     * 查询电子书信息(按照名称、标题排序)
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData handleMeBook() throws Exception;

    /**
     * 查询电子书编码
     *
     * @return ResultData
     * @throws Exception 异常
     */
    List<Integer> getAllCode() throws Exception;

    /**
     * 根据编码查询电子书信息
     *
     * @param code 电子书编码
     * @return ResultData
     * @throws Exception 异常
     */
    int getCountByCode(int code) throws Exception;

    /**
     * 分页查询电子书
     *
     * @param title    标题
     * @param author   作者
     * @param category 类别
     * @param descr    描述
     * @param source   来源
     * @param pageNum  页码
     * @param pageSize 记录数
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData queryBook(String title, String author, String category, String descr, String source, int pageNum, int pageSize) throws Exception;


}
