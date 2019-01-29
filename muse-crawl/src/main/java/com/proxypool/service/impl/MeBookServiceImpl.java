package com.proxypool.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.proxypool.dao.MeBookInfoMapper;
import com.proxypool.kindlebook.MeBookInfo;
import com.proxypool.service.MeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

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

    @Override
    public ResultData queryBook(String title, String author, String category, String descr, int pageNum,
                                int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);

        Map<String, Object> params = Maps.newHashMap();
        if (!StringUtils.isEmpty(title)) {
            params.put("title", title);
        }
        if (!StringUtils.isEmpty(author)) {
            params.put("author", author);
        }
        if (!StringUtils.isEmpty(category)) {
            params.put("category", category);
        }
        if (!StringUtils.isEmpty(descr)) {
            params.put("detailDesc", descr);
        }

        ResultData bookResult;
        List<MeBookInfo> bookList = mapper.queryMeBook(params);
        if (bookList != null && bookList.size() > 0) {
            bookResult = ResultData.getSuccessResult(new PageInfo<>(bookList));
        } else {
            bookResult = ResultData.getSuccessResult("没有找到匹配的电子书");
        }
        return bookResult;
    }


}
