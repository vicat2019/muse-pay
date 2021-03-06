package com.proxypool.controller;

import com.google.common.collect.Maps;
import com.muse.common.entity.ResultData;
import com.muse.common.util.HttpUtils;
import com.proxypool.component.PhantomJsDownloader;
import com.proxypool.config.GuavaCacheUtil;
import com.proxypool.kindlebook.KdlBookProcessor;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.picture.PictureInfoPipeline;
import com.proxypool.picture.WallhavenProcessor;
import com.proxypool.recruit.Job51Processor;
import com.proxypool.recruit.RecruitInfoPipeline;
import com.proxypool.registration.Doctor160JSONProcess;
import com.proxypool.secretgarden.SecretGardenPipeline;
import com.proxypool.secretgarden.SecretGardenProcessor;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 测试-控制类
 * @author: Vincent
 * @create: 2018-12-18 10:21
 **/
@RequestMapping("/crawl")
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

    @Autowired
    private SecretGardenProcessor secretGardenProcessor;

    @Autowired
    private Job51Processor proxy51jobProcessor;


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


    @Autowired
    private SecretGardenPipeline secretGardenPipeline;

    @RequestMapping("/sg")
    public void secretGarden() {
        try {
            Map<String, String> cookiesMap = Maps.newHashMap();
            cookiesMap.put("__cfduid", "d2a8a7ca83f1eb76bdb445da62267316f1549868344");
            secretGardenProcessor.execute(secretGardenPipeline, false, cookiesMap, new PhantomJsDownloader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Autowired
    private RecruitInfoPipeline recruitInfoPipelinel;

    @RequestMapping("/testre")
    public void testRecurite() {
        try {
            // 处理页面的时间间隔
            proxy51jobProcessor.setInterval(700).setThreadCount(5);
            proxy51jobProcessor.execute(recruitInfoPipelinel, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public RestTemplate restTemplate;


    @Autowired
    private HttpUtils httpUtils;

    @RequestMapping("/download")
    public void dp() {
        String url = "https://www.skuimg.com/u/20190215/10260129.jpg";

        System.out.println(httpUtils.get(url, Maps.newHashMap()));

    }

    @Autowired
    private Doctor160JSONProcess doctor160JSONProcess;

    @RequestMapping("/doctor")
    public void doctor() {
        System.out.println("-------------------------------");
        try {

            doctor160JSONProcess.execute(null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
