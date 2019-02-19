package com.proxypool.service;

import com.proxypool.secretgarden.SgDataInfo;

import java.util.List;

/**
 * Created by Vincent on 2018/12/17.
 */
public interface SgDataInfoService {

    int insertBatch(List<SgDataInfo> dataList) throws Exception;

    int updateStatusBatch(List<Integer> codeList) throws Exception;

}
