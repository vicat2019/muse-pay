package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.config.GuavaCacheUtil;
import com.proxypool.kindlebook.KdlBookProcessor;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.picture.PictureInfoPipeline;
import com.proxypool.picture.WallhavenProcessor;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RecruitInfoService;
import com.proxypool.service.RpSequenceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private MebookProcessor mebookProcessor;

    @Autowired
    private MeBookPipeline meBookPipeline;

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private WallhavenProcessor wallhavenProcessor;

    @Autowired
    private PictureInfoPipeline pictureInfoPipeline;

    @Autowired
    private RecruitInfoService recruitInfoService;

    @Autowired
    private KdlBookProcessor kdlBookProcessor;


    @RequestMapping("/sequence/{count}")
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
            mebookProcessor.setInterval(600).setThreadCount(3).execute(null, null);
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
        if (codeList != null) {
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

    @RequestMapping("/pic")
    public ResultData test() {
        crawlPicture();

        return ResultData.getSuccessResult();
    }

    @Async("executor")
    public void crawlPicture() {
        try {
            wallhavenProcessor.setInterval(800).setThreadCount(3).execute(pictureInfoPipeline, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/rinse")
    public ResultData rinse() {
        try {
            long start = System.currentTimeMillis();
            ResultData result = recruitInfoService.rinseRecruit(1, 5000);
            log.info("清洗数据耗时=" + (System.currentTimeMillis() - start));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult(e.getMessage());
        }
    }


    @RequestMapping("/kdlbook")
    public void kdlbook() {
        try {
            kdlBookProcessor.setInterval(800).setThreadCount(3).execute(meBookPipeline, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
