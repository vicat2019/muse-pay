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


    /**
     * 添加电子书信息
     *
     * @param bookInfo 电子书信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData insert(MeBookInfo bookInfo) throws Exception {
        log.info("insert() 保存电子书信息：" + ((bookInfo != null) ? bookInfo : "null"));
        if (bookInfo == null || !bookInfo.canSave()) {
            return ResultData.getErrResult("参数异常");
        }

        // 保存到库中
        int count = mapper.insertOrUpdate(bookInfo);
        log.info("insert() 保存电子书信息=" + bookInfo.getTitle() + ", 结果=" + count);

        return ResultData.getSuccessResult(count);
    }

    /**
     * 更新电子书信息
     *
     * @param bookInfo 电子书信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData update(MeBookInfo bookInfo) throws Exception {
        log.info("update() 更新电子书信息：" + (bookInfo != null ? bookInfo.toString() : "null"));
        if (bookInfo == null || !bookInfo.canSave()) {
            return ResultData.getErrResult("参数异常");
        }

        // 保存到库中
        int count = mapper.updateByPrimary(bookInfo);
        log.info("update() 更新电子书信息=" + bookInfo.getTitle() + ", 结果=" + count);

        return ResultData.getSuccessResult(count);
    }

    /**
     * 分页查询电子书信息
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
    @Override
    public ResultData queryBook(String title, String author, String category, String descr, String source, int pageNum,
                                int pageSize) throws Exception {
        log.info("queryBook() 分页查询电子书：title=" + title + ", author=" + author + ", category=" + category
                + ", descr=" + descr + ", pageNum=" + pageNum + ", pageSize=" + pageSize);

        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        // 组装参数集合
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
        if (!StringUtils.isEmpty(source)) {
            params.put("source", source);
        }

        // 查询并返回结果
        ResultData bookResult;
        List<MeBookInfo> bookList = mapper.queryMeBook(params);
        log.info("queryBook() 分页查询电子书结果=" + (bookList == null ? "0" : bookList.size()));

        if (bookList != null && bookList.size() > 0) {
            bookResult = ResultData.getSuccessResult(new PageInfo<>(bookList));
        } else {
            bookResult = ResultData.getSuccessResult("没有找到匹配的电子书");
        }
        return bookResult;
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
