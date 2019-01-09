package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.component.ProxyDownloader;
import com.proxypool.config.GuavaCacheUtil;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.recruit.TestProcessor;
import com.proxypool.service.MeBookService;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RpSequenceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 测试-控制类
 * @author: Vincent
 * @create: 2018-12-18 10:21
 **/
@RequestMapping("/test")
@RestController
public class TestController {
    private Logger log = LoggerFactory.getLogger("TestController");

    @Autowired
    private RpSequenceInfoService sequenceInfoService;

    @Autowired
    private ProxyDownloader proxyDownloader;

    @Autowired
    private TestProcessor testProcessor;

    @Autowired
    private MebookProcessor mebookProcessor;

    @Autowired
    private MeBookPipeline meBookPipeline;

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private MeBookService meBookService;


    @RequestMapping("/a")
    public String test() {
        String aa = "";
        try {
            aa = sequenceInfoService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aa;
    }

    @RequestMapping("/p")
    public ResultData proxySpider() {
        try {
            testProcessor.execute(null, proxyDownloader.setRetryCount(5));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }


    @RequestMapping("/{count}")
    public ResultData obtainSequence(@PathVariable int count) {
        try {
            for (int i = 0; i < count; i++) {
                String sequence = sequenceInfoService.obtainSequence();
                log.info("SEQUENCT_OBTAIN_=" + sequence);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }


    @RequestMapping("/mebook")
    public ResultData mebookProcessor() {
        try {
            mebookProcessor.setInterval(1000).setThreadCount(1).execute(meBookPipeline, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }

    @RequestMapping("/check")
    public ResultData checkProxy() {
        try {
            return proxyIpInfoService.checkAvailability(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }

    @RequestMapping("/handle")
    public ResultData handleData() {
        List<Integer> codeList = GuavaCacheUtil.cache.getUnchecked("code");
        if (codeList!= null) {
            if (codeList.size() > 100) {
                codeList.subList(0, 100).forEach(System.out::println);
            } else {
                codeList.forEach(System.out::println);
            }
        } else {
            System.out.println("codeList is empty");
        }

        return ResultData.getSuccessResult();
    }


}
