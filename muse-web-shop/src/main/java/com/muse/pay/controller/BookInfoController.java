package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.BookInfo;
import com.muse.pay.service.BookInfoService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by code generator  on 2018/07/22.
 */
@Controller
@RequestMapping("/book")
public class BookInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger("BookInfoController");

    @Autowired
    private BookInfoService bookInfoService;

    @RequestMapping("/add")
    public ResultData add(@Valid BookInfo bookInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            return resultData;
        }

        try {
            return bookInfoService.add(bookInfo);
        } catch (Exception e) {
            logger.error("添加数据异常=" + e.getMessage());
            return ResultData.getErrResult("添加失败");
        }
    }

    @RequestMapping("/delete")
    public ResultData delete(@RequestParam Integer id) {
        try {
            return bookInfoService.del(id);
        } catch (Exception e) {
            logger.error("删除数据异常=" + e.getMessage());
            return ResultData.getErrResult("删除失败");
        }
    }

    @RequestMapping("/update")
    public ResultData update(@Valid BookInfo bookInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            return resultData;
        }

        try {
            return bookInfoService.update(bookInfo);
        } catch (Exception e) {
            logger.error("更新数据异常=" + e.getMessage());
            return ResultData.getErrResult("更新失败");
        }
    }

    @RequestMapping("/detail")
    public ResultData detail(Integer id) {
        try {
            return bookInfoService.get(id);
        } catch (Exception e) {
            logger.error("查询数据异常=" + e.getMessage());
            return ResultData.getErrResult("查询失败");
        }
    }

    @RequestMapping("/list")
    @RequiresAuthentication
    public String queryList(@RequestParam(required = false) BookInfo bookInfo,
                            @RequestParam(required = false, defaultValue = "1") int pageNum,
                            @RequestParam(required = false, defaultValue = "10") int pageSize,
                            ModelMap modelMap) {
        try {

            ResultData resultData = bookInfoService.list(bookInfo, pageNum, pageSize);
            modelMap.addAttribute("data", resultData.getData());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询数据异常=" + e.getMessage());
        }

        return "book/index";
    }

    @RequestMapping("/recommend")
    @RequiresAuthentication
    public String queryRecommend(@RequestParam(required = false, defaultValue = "20") int size, ModelMap modelMap) {

        try {
            ResultData resultData = bookInfoService.getRecommend(size);
            if (!resultData.isOk() || resultData.resultIsEmpty()) {
                modelMap.addAttribute("errMsg", resultData.getMessage());
            }

            List<BookInfo> bookList = (List<BookInfo>) resultData.getData();
            modelMap.addAttribute("data", bookList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取推荐数据异常=" + e.getMessage());
        }

        return "book/recommend";
    }


}
