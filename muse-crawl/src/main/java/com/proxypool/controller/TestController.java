package com.proxypool.controller;

import com.google.common.collect.Maps;
import com.muse.common.entity.ResultData;
import com.proxypool.config.GuavaCacheUtil;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.picture.PictureInfoPipeline;
import com.proxypool.picture.WallhavenProcessor;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RpSequenceInfoService;
import com.proxypool.weibo.SeleniumDownloader;
import com.proxypool.weibo.WeiboProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.List;
import java.util.Map;

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
    private WeiboProcessor weiboProcessor;


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
            mebookProcessor.setInterval(600).setThreadCount(3).execute(meBookPipeline, null);
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


    @RequestMapping("/wb")
    public void weibo() {
        try {
            Map<String, String> cookiesMap = Maps.newHashMap();
            cookiesMap.put("_s_tentry", "coolshell.cn");
            cookiesMap.put("Apache", "8916251159689.412.1548664241703");
            cookiesMap.put("cross_origin_proto", "SSL");
            cookiesMap.put("login_sid_t", "58449da82a4af9663cc41384c7c28b9e");
            cookiesMap.put("SINAGLOBAL", "8916251159689.412.1548664241703");
            cookiesMap.put("SUB", "_2AkMrEjKLf8PxqwJRmPAVym_naI5xwgrEieKdTsNQJRMxHRl-yT83qhNctRB6AJIcZJMb-_4bsFAFvxKcSjqFhN7Vw03I");
            cookiesMap.put("SUBP", "0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhL1fsMaWrUzwy7dexzkaf0");
            cookiesMap.put("Ugrow-G0", "9642b0b34b4c0d569ed7a372f8823a8e");
            cookiesMap.put("ULV", "1548664241705:1:1:1:8916251159689.412.1548664241703:");
            cookiesMap.put("UOR", "coolshell.cn,widget.weibo.com,coolshell.cn");
            cookiesMap.put("wb_view_log", "1920*10801");
            cookiesMap.put("WBStorage", "7ddc7b17c6f27c11|undefined");
            cookiesMap.put("YF-Page-G0", "86b4280420ced6d22f1c1e4dc25fe846");
            cookiesMap.put("YF-V5-G0", "572595c78566a84019ac3c65c1e95574");

            System.getProperties().setProperty("webdriver.chrome.driver", "D:/chromedriver/chromedriver.exe");


            Downloader downloader = new SeleniumDownloader("D:/chromedriver/chromedriver.exe");
            weiboProcessor.execute(null, false, cookiesMap, downloader);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
