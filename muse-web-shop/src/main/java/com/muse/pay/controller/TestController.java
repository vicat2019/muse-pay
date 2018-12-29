package com.muse.pay.controller;

import com.muse.common.entity.ResultData;
import com.muse.pay.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 测试Controller
 * @author: Vincent
 * @create: 2018-10-06 15:42
 **/
@RestController
@RequestMapping("/random")
public class TestController {

    @Autowired
    private BookInfoService bookInfoService;


    /**
     * 生成数据文件
     *
     * @param size 记录数
     * @param path 文件路径
     * @return ResultData
     */
    @RequestMapping("/datafile")
    public ResultData randomFile(int size, String path) {
        ResultData result;
        try {

            result = bookInfoService.genRandomBookFile(size, path);

        } catch (Exception e) {
            e.printStackTrace();
            result = ResultData.getErrResult(e.getMessage());
        }

        return result;

    }


}
