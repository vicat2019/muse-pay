package com.proxypool.service.impl;

import com.google.common.base.Preconditions;
import com.muse.common.service.BaseService;
import com.proxypool.dao.SgDataInfoMapper;
import com.proxypool.secretgarden.SgDataInfo;
import com.proxypool.service.SgDataInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Vincent on 2018/12/17.
 */
@Service("sgDataInfoService")
@Transactional
public class SgDataInfoServiceImpl extends BaseService<SgDataInfoMapper, SgDataInfo> implements SgDataInfoService {
    private Logger log = LoggerFactory.getLogger("SgDataInfoServiceImpl");


    @Override
    public int insertBatch(List<SgDataInfo> dataList) throws Exception {
        Preconditions.checkNotNull(dataList);
        Preconditions.checkArgument(dataList.size() > 0, "参数不能为空");

        return mapper.insertBatch(dataList);
    }

    @Override
    public int updateStatusBatch(List<Integer> codeList) throws Exception {
        Preconditions.checkNotNull(codeList);
        Preconditions.checkArgument(codeList.size() > 0, "参数不能为空");

        return mapper.updateStatusBatch(codeList);
    }
}
