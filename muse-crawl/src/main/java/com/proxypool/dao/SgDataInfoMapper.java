package com.proxypool.dao;


import com.proxypool.secretgarden.SgDataInfo;

import java.util.List;

/**
 * @Description: 序列信息DAO类
 * @Author: Vincent
 * @Date: 2019/1/25
 */
public interface SgDataInfoMapper {

    int updateByPrimaryKey(SgDataInfo sgDataInfo);

    int insert(SgDataInfo sgDataInfo);

    int delete(int id);

    int insertBatch(List<SgDataInfo> list);

    int updateStatusBatch(List<Integer> codeList);


}