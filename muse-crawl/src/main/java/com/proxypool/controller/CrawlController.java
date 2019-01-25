package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.proxyip.*;
import com.proxypool.recruit.Job51Processor;
import com.proxypool.recruit.RecruitInfoPipeline;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RecruitInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 抓取信息控制类
 * @author: Vincent
 * @create: 2018-10-15 12:06
 **/
@RequestMapping("/crawl")
@RestController
public class CrawlController {
    private Logger log = LoggerFactory.getLogger("CrawlController");

    @Autowired
    private ProxyPipeline proxyPipeline;

    @Autowired
    private RecruitInfoPipeline recruitInfoPipeline;

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private ProxyProcessor proxyProcessor;

    @Autowired
    private IP89Processor ip89Processor;

    @Autowired
    private IP66Processor ip66Processor;

    @Autowired
    private Proxy31fProcessor proxy31fProcessor;

    @Autowired
    private ProxyJiangxianliProcessor proxyJiangxianliProcessor;

    @Autowired
    private ProxyKuaidailiProcessor proxyKuaidailiProcessor;

    @Autowired
    private Job51Processor proxy51jobProcessor;

    @Autowired
    private RecruitInfoService recruitInfoService;

    @Autowired
    private MebookProcessor mebookProcessor;

    @Autowired
    private MeBookPipeline meBookPipeline;


    /**
     * 定时抓取JL
     *
     * @return ResultData
     */
    @Scheduled(cron = "${RECRUIT_CRON}")
    public void recruit() {
        try {
            // 处理页面的时间间隔
            proxy51jobProcessor.setInterval(700).setThreadCount(5);
            proxy51jobProcessor.execute(recruitInfoPipeline, false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("抓取JL异常=" + e.getMessage());
        }
    }

    /**
     * 定时清理重复的记录
     */
    @Scheduled(cron = "${DEL_REPEAT_RECRUIT_CRON}")
    public void delRepeatRecruit() {
        try {
            ResultData result = recruitInfoService.delRepeatRecruit();
            log.info("定时清理重复记录结果=" + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("抓取JL异常=" + e.getMessage());
        }
    }

    /**
     * 抓取代理IP
     */
    @Scheduled(cron = "${PROXY_IP_EXECUTE_CRON}")
    public void execute() {
        try {
            proxyProcessor.execute();
            ip89Processor.execute();

            ip66Processor.execute(proxyPipeline, false);
            proxy31fProcessor.execute(proxyPipeline, false);
            proxyJiangxianliProcessor.execute(proxyPipeline, false);
            proxyKuaidailiProcessor.execute(proxyPipeline, false);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("抓取代理IP异常=" + e.getMessage());
        }
    }

    /**
     * 抓取KD电子书信息
     */
    @Scheduled(cron = "${CRAWL_BOOK_CRON}")
    public void mebookProcessor() {
        try {
            mebookProcessor.setInterval(1000).setThreadCount(3).execute(meBookPipeline, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证代理是否可用
     */
    @Scheduled(cron = "${CHECK_PROXY_IP_CRON}")
    public void checkScheduled() {
        try {
            proxyIpInfoService.checkAvailability(2000);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("验证代理是否可用异常=" + e.getMessage());
        }
    }


}
