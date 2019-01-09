package com.proxypool.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.proxypool.dao.MeBookInfoMapper;
import com.proxypool.kindlebook.MeBookInfo;
import com.proxypool.service.MeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 电子书服务类
 * @author: Vincent
 * @create: 2019-01-04 15:15
 **/
@Service("meBookService")
public class MeBookServiceImpl extends BaseService<MeBookInfoMapper, MeBookInfo> implements MeBookService {
    private Logger log = LoggerFactory.getLogger("MeBookServiceImpl");


    @Override
    public ResultData insert(MeBookInfo bookInfo) throws Exception {
        if (bookInfo == null || !bookInfo.canSave()) {
            log.error("保存数据，参数异常，title=" + bookInfo.getTitle() + ", downloadUrl=" + bookInfo.getDownloadUrl());
            return ResultData.getErrResult("参数异常");
        }

        // 保存到库中
        int count = mapper.insertOrUpdate(bookInfo);
        log.info("保存数据信息=" + bookInfo.getTitle() + ", 结果=" + count);

        return ResultData.getSuccessResult(count);
    }

    @Override
    public ResultData update(MeBookInfo bookInfo) throws Exception {
        if (bookInfo == null || !bookInfo.canSave()) {
            log.error("参数异常，bookInfo=" + (bookInfo != null ? bookInfo.toString() : "null"));
            return ResultData.getErrResult("参数异常");
        }

        // 保存到库中
        int count = mapper.updateByPrimary(bookInfo);
        log.info("更新内容=" + bookInfo.getTitle() + ", 结果=" + count);

        return ResultData.getSuccessResult(count);
    }

    @Override
    public ResultData handleMeBook() throws Exception {

        return ResultData.getSuccessResult();
    }

    @Override
    public List<Integer> getAllCode() throws Exception {
        return mapper.getAllCode();
    }

    @Override
    public int getCountByCode(int code) throws Exception {
        return mapper.getCountByCode(code);
    }


}
